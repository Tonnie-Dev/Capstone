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
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepClickListener
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.*
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
        interval = TimeUnit.SECONDS.toMillis(3)
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

  // TODO: Declare ViewModel

  private val viewModel: RepresentativeViewModel by viewModels()
  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {

    // TODO: Establish bindings
    binding = FragmentRepresentativeBinding.inflate(inflater)

    // make binding observe LiveData
    binding.lifecycleOwner = viewLifecycleOwner

    // link binding's viewModel to viewModel
    binding.viewModel = viewModel

    // initialize FusedLocationProviderClient
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    // Define and assign Representative adapter
    binding.repsRecyclerview.adapter = RepresentativeListAdapter(RepClickListener {})

    viewModel.showSnackBar.observe(viewLifecycleOwner) { message ->
      Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    // TODO: Populate Representative adapter

    // TODO: Establish button listeners for field and location search
    // search  by address
    binding.buttonSearch.setOnClickListener {}

    // use my location button
    binding.buttonLocation.setOnClickListener {

      // request for permission if not already granted
      clearForm()
      fineLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    return binding.root
  }

  override fun onStop() {
    super.onStop()

    // stop updates
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

            setUpAddressAndGetReps()
          } else {
            Timber.i("The lastLoc is null")
            // prompt user to turn on location

            showLocationSettingDialog()

            // when user turns on location trigger updates to get a location

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper())
          }

          // in case of error Toast the error in a short Toast message
        }
        .addOnFailureListener {
          Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_SHORT).show()
        }
  }

  private fun setUpAddressAndGetReps() {

    // get geoCoded address String
    val address = geoCodeLocation(lastKnownLocation)

      //format the geoCoded Address
      val formattedAddress = address.toFormattedString()

      // Address outside USA
      if (formattedAddress == "") {

          viewModel.invalidateAddress(getString(R.string.invalid_address))
          clearForm()
      }else {

          // set up viewModel's MutableLiveData for address
          viewModel.getAddressFromGeoLocation(address)

          // get network response for the reps
          viewModel.fetchRepsFromNetwork(formattedAddress)

          autoFillAddresses(address)
      }





   






  }

  private fun autoFillAddresses(address: Address) {

    if (address.line1 == null) {
      binding.addressLine1.setText(getString(R.string.unnamed_address))
      Timber.i("The unnamed address is ${address.line1}")
    } else {

      binding.addressLine1.setText(address.line1)
      binding.addressLine2.setText(address.line2)
      binding.city.setText(address.city)
      binding.zip.setText(address.zip)
    }

    Timber.i("The full address is $address")
  }

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
              address.postalCode)
        }
        .first()
  }

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
                        Snackbar.LENGTH_INDEFINITE)
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
            binding.root, getString(R.string.location_required_error), Snackbar.LENGTH_INDEFINITE)
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

  // settings Dialog
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
                        binding.root, R.string.location_required_error, Snackbar.LENGTH_INDEFINITE)
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
