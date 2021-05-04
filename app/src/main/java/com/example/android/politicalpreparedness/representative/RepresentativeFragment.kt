package com.example.android.politicalpreparedness.representative

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.*
import java.util.concurrent.TimeUnit
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.BuildConfig

class DetailFragment : Fragment() {

    //VARS
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var lastKnownLocation: Location


    //Location Components
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val locationRequest = LocationRequest().apply {

        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = TimeUnit.MINUTES.toMillis(5)
        fastestInterval = TimeUnit.SECONDS.toMillis(1)
    }


    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            if (locationResult != null) {

                lastKnownLocation = locationResult.lastLocation

            }
        }
    }


    private val fineLocationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {


        isGranted ->

        if (isGranted) {
            //get last known location permission

            getLastKnownLocation()
        } else {

            showRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }



    companion object {
        //TODO: Add Constant for Location request

        private val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = TimeUnit.MINUTES.toMillis(5)
            fastestInterval = TimeUnit.MINUTES.toMillis(3)
        }
    }

    //TODO: Declare ViewModel

    private val viewModel: RepresentativeViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //TODO: Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater)

        //make binding observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        //link binding's viewModel to viewModel
        binding.viewModel = viewModel

        //initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                requireActivity())

        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search
        //search  by address
        binding.buttonSearch.setOnClickListener {}

        //use my location button
        binding.buttonLocation.setOnClickListener {

            //request for permission if not already granted

            fineLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)


        }


        return binding.root

    }


    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        //TODO: Get location from LocationServices
        //get last known location from fusedLocationProviderClient returned as a task
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {

            lastLoc ->

            if (lastLoc != null) {

                //initialize lastKnownLocation from fusedLocationProviderClient
                lastKnownLocation = lastLoc
            } else {

                //prompt user to turn on location

                showLocationSettingDialog()

                //when user turns on location trigger updates to get a location

                fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                                                   locationCallback,
                                                                   Looper.getMainLooper())
            }

            //in case of error Toast the error in a short Toast message
        }.addOnFailureListener {


            Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_SHORT).show()
        }
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address

        val address = geoCodeLocation(lastKnownLocation)



    }

    private fun autoFillAddresses(){

        


    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare,
                            address.subThoroughfare,
                            address.locality,
                            address.adminArea,
                            address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }




    private fun showRationale(permission: String) {
        if (shouldShowRequestPermissionRationale(permission)) {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(
                    requireActivity()
            ).setTitle("Location Permission")
                    .setMessage(getString(R.string.rationale_for_location_permissions))
                    .setPositiveButton(getString(R.string.settings)) { dialog, _ ->

                        startActivity(Intent().apply {

                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })

                        dialog.dismiss()

                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        Snackbar.make(
                                binding.root, getString(R.string.location_required_error),
                                Snackbar.LENGTH_INDEFINITE
                        )
                                .setAction(android.R.string.ok) {

                                    showSnackBar(
                                            getString(R.string.rationale_for_location_permissions)
                                    )
                                }

                    }
                    .create()
            materialAlertDialogBuilder.show()

        }
    }


    private fun showSnackBar(message: String) {
        Snackbar.make(
                binding.root, getString(R.string.location_required_error),
                Snackbar.LENGTH_INDEFINITE
        )
                .setAction(getString(R.string.settings)) {
                    startActivity(Intent().apply {

                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })

                }
                .show()

    }

    //Check if location is enabled
    @RequiresApi(Build.VERSION_CODES.M)
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(LocationManager::class.java)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    //settings Dialog
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showLocationSettingDialog() {

        if (!isLocationEnabled()) {

            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(getString(R.string.enable_location))
                    .setMessage(R.string.enable_dialog_message)
                    .setPositiveButton(getString(R.string.settings)) { _, _ ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                    .setNegativeButton(getString(R.string.exit)) { _, _ ->
                        Snackbar.make(
                                binding.root, R.string.location_required_error, Snackbar
                                .LENGTH_INDEFINITE
                        )
                                .setAction(getString(android.R.string.ok)) {}
                                .show()
                    }
                    .create()
            materialAlertDialogBuilder.show()
        }
    }
}


//OLD CODE for android permissions

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
