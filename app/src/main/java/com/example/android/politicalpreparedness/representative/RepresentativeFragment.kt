package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.gms.location.*
import java.util.Locale
import java.util.concurrent.TimeUnit

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

        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)


            if (result != null) {


            }
        }
    }


    private val fineLocationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {


        isGranted ->

        if (isGranted) {
            //get last known location permission

        } else {

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
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
        binding.buttonLocation.setOnClickListener { }


        return binding.root

    }


    override fun onStart() {
        super.onStart()

        //register for permissions


    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
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
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
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
    }*/