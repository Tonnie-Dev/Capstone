package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeViewModel: ViewModel() {

    //TODO: Establish live data for representatives and address
    
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
    get() = _representatives

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
