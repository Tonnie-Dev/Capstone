package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepresentativeViewModel(application: Application) : AndroidViewModel(application) {

  // TODO: Establish live data for representatives and address

  private val _reps = MutableLiveData<List<Representative>>()
  val reps: LiveData<List<Representative>>
    get() = _reps

  private val _address = MutableLiveData<Address>()
  val address: LiveData<Address>
    get() = _address

  private val _status = MutableLiveData<LoadingStatus>()
  val status: LiveData<LoadingStatus>
    get() = _status

  private val _stateSpinnerValue = MutableLiveData<String?>()
  val stateSpinnerValue: LiveData<String?>
    get() = _stateSpinnerValue

  private val _showSnackbarValue = MutableLiveData<String>()
  val showSnackBar: LiveData<String>
    get() = _showSnackbarValue

  private val _line1 = MutableLiveData<String>("baop")
  val line1: LiveData<String>
  get() = _line1
  var edLine1 = ""

  private val _line2 = MutableLiveData<String>()
  val line2: LiveData<String>
  get() = _line2
  var edLine2 = ""

  private val _city = MutableLiveData<String>()
  val city: LiveData<String>
  get() = _city
  var edCity = ""

  private val _zipcode = MutableLiveData<String>()
  val zip: LiveData<String>
  get() = _zipcode
  var edZipCode = ""

  fun fetchRepsFromNetwork(address: String) {

    viewModelScope.launch {
      try {

        _status.value = LoadingStatus.LOADING
        withContext(IO) {
          val (offices, officials) = CivicsApi.retrofitService.representativeInfoByAddress(address)
          _reps.postValue(offices.flatMap { office -> office.getRepresentatives(officials) })
          _status.postValue(LoadingStatus.FINISHED)
        }
      } catch (e: Exception) {

        _status.value = LoadingStatus.ERROR
      }
    }
  }




  // Create function to fetch representatives from API from a provided address

 /* fun fetchRepsFromNetwork(address: String) {

    viewModelScope.launch {
      try {Timber.i("Entering Try-Catch Block")

        _status.value = LoadingStatus.LOADING
        Timber.i("the state at inside catch is ${ _status.value}")

        //switch to background thread
        withContext(IO) {
          Timber.i("Entering IO Block")
          val (offices, officials) = CivicsApi.retrofitService.representativeInfoByAddress(address)
          _reps.postValue(offices.flatMap { office -> office.getRepresentatives(officials) })

          _status.postValue(LoadingStatus.FINISHED)
          Timber.i("Leaving IO Block")
          Timber.i("the state at IO is ${ _status.value}")

        }




      } catch (e: Exception) {

        _status.value = LoadingStatus.ERROR
      }

    }
    Timber.i("the state outside scope  is ${ _status.value}")
  }*/

  /**
   * The following code will prove helpful in constructing a representative from the API. This code
   * combines the two nodes of the RepresentativeResponse into a single official :
   *
   * val (offices, officials) = getRepresentativesDeferred.await() _representatives.value =
   * offices.flatMap { office -> office.getRepresentatives(officials) }
   *
   * Note: getRepresentatives in the above code represents the method used to fetch data from the
   * API Note: _representatives in the above code represents the established mutable live data
   * housing representatives
   */
  fun invalidateAddress(message: String) {
    _address.value = Address()
    _showSnackbarValue.value = message
    _reps.value = listOf()
    _stateSpinnerValue.value = "Invalid"
    _status.value = LoadingStatus.ERROR

    Timber.i("Following Invalidate the spinner points to ${_stateSpinnerValue.value}")
    Timber.i("Following Invalidate address is now ${_address.value}")
  }
  // TODO: Create function get address from geo location
  fun getAddressFromGeoLocation(address: Address) {
    _address.value = address
    _stateSpinnerValue.value = address.state

    Timber.i("The Spinner is pointiing to ${_stateSpinnerValue.value}")
  }

  // TODO: Create function to get address from individual fields

 fun getAddressFromForm(){

    _line1.value = edLine1
    _line2.value = edLine2
    _city.value = edCity
    _zipcode.value = edZipCode

    //construct a new address

    _address.value = Address(edLine1, edLine2, edCity, _stateSpinnerValue.value, edZipCode)


  }
}

enum class LoadingStatus {
  LOADING,
  ERROR,
  FINISHED
}
