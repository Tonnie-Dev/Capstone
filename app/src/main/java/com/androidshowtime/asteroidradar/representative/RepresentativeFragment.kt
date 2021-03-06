package com.androidshowtime.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.androidshowtime.BuildConfig
import com.androidshowtime.R
import com.androidshowtime.databinding.FragmentRepresentativeBinding
import com.androidshowtime.network.models.Address

import com.androidshowtime.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.*
import java.util.concurrent.TimeUnit
import timber.log.Timber

class DetailFragment : Fragment() {

    // VARS
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var lastKnownLocation: Location

    // LOCATION COMPONENTS
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val locationRequest =
        LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = TimeUnit.MINUTES.toMillis(3)
            fastestInterval = TimeUnit.SECONDS.toMillis(1)
        }


    private val locationCallback =
        object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                if (locationResult != null) {

                    lastKnownLocation = locationResult.lastLocation

                   getLastKnownLocation()
                }
            }
        }

    // permissions check
    private val fineLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

                // get last known location permission
                getLastKnownLocation()
            } else {

                showRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

    // Declare ViewModel

    private val viewModel: RepresentativeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //rename the title bar
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.reps_fragment_string)

        // Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater)

        // make binding observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        // link binding's viewModel to viewModel
        binding.viewModel = viewModel

        // initialize FusedLocationProviderClient
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        // Define and assign Representative adapter
        binding.repsRecyclerview.adapter = RepresentativeListAdapter()

        //Observe and show Snackbar accordingly
        viewModel.showSnackBar.observe(viewLifecycleOwner) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()

        }

        // Establish button listeners for field and location search

        // perform search using address form
        binding.buttonSearch.setOnClickListener {

            viewModel.getAddressFromForm()
            setUpAddressAndGetReps(viewModel.address.value!!)
            hideKeyboard()
        }

        // perform search using geolocation
        binding.buttonLocation.setOnClickListener {

            // request for permission if not already granted
            fineLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            hideKeyboard()
            clearForm()
        }


        viewModel.status.observe(viewLifecycleOwner){

            Timber.i("the status on observation is $it")
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()

        // stop updates when in background
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {

        // get last known location from fusedLocationProviderClient returned as a task

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { lastLoc ->
                if (lastLoc != null) {

                    // initialize lastKnownLocation from fusedLocationProviderClient
                    lastKnownLocation = lastLoc

                    // get geoCoded address String
                    val address = geoCodeLocation(lastKnownLocation)

                    setUpAddressAndGetReps(address)

                } else {

                    // prompt user to turn on location
                    showLocationSettingDialog()

                    // when user turns on location trigger updates to get a location
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest, locationCallback, Looper.getMainLooper()
                    )


                }

                // in case of error Toast the error in a short Toast message
            }
            .addOnFailureListener {

                Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun setUpAddressAndGetReps(address: Address) {


        //format the geoCoded Address
        val formattedAddress = address.toFormattedString()


        // Address outside USA
        if (formattedAddress == "") {

            viewModel.invalidateAddress(getString(R.string.invalid_address))
            clearForm()

            //For valid addresses set address an prompt an API Call
        } else {

            // set up viewModel's MutableLiveData for address
            viewModel.getAddressFromGeoLocation(address)

            // get network response for the reps
            viewModel.fetchRepsFromNetwork(formattedAddress)

            //fill forms with the valid address
            autoFillAddresses(address)
        }

    }

    private fun autoFillAddresses(address: Address) {

        //Name addressLine1 if null
        if (address.line1 == null) {

            binding.addressLine1.setText(getString(R.string.unnamed_address))

        } else {

            //set texts on EditTexts
            binding.addressLine1.setText(address.line1)
            binding.addressLine2.setText(address.line2)
            binding.city.setText(address.city)
            binding.zip.setText(address.zip)
        }


    }

    //get human readable address
    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder
            .getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

//Clear the form
    private fun clearForm() {
        binding.addressLine1.setText("")
        binding.addressLine2.setText("")
        binding.city.setText("")
        binding.zip.setText("")
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    //Educate the user on the need for Location Permission
    private fun showRationale(permission: String) {
        if (shouldShowRequestPermissionRationale(permission)) {
            val materialAlertDialogBuilder =
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Location Permission")
                    .setMessage(getString(R.string.rationale_for_location_permissions))
                    .setPositiveButton(getString(R.string.settings)) { dialog, _ ->
                        startActivity(
                            Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            })

                        dialog.dismiss()
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        Snackbar.make(
                            binding.root,
                            getString(R.string.location_required_error),
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction(android.R.string.ok) {
                                showSnackBar(getString(R.string.rationale_for_location_permissions))
                                dialog.dismiss()
                            }
                            .show()
                    }
                    .create()

            materialAlertDialogBuilder.show()
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root, getString(R.string.location_required_error), Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.settings)) {
                startActivity(
                    Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
            }
            .show()
    }

    // Check if location is enabled
    @RequiresApi(Build.VERSION_CODES.M)
    private fun isLocationEnabled(): Boolean {

        val locationManager = requireActivity().getSystemService(LocationManager::class.java)

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // show Location Settings Dialog
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showLocationSettingDialog() {

        if (!isLocationEnabled()) {

            val materialAlertDialogBuilder =
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(getString(R.string.enable_location))
                    .setMessage(R.string.enable_dialog_message)
                    .setPositiveButton(getString(R.string.settings)) { _, _ ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                    .setNegativeButton(getString(R.string.exit)) { _, _ ->
                        Snackbar.make(
                            binding.root,
                            R.string.location_required_error,
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction(getString(android.R.string.ok)) {}
                            .show()
                    }
                    .create()

            materialAlertDialogBuilder.show()
        }
    }




}
























// OLD CODE for android permissions


/*
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         Handle location permission result to get location on permission granted
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            // Request Location permissions
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //Check if permission is already granted and return (true = granted, false = denied/other)
    }
private fun getLocation() {







}*/
