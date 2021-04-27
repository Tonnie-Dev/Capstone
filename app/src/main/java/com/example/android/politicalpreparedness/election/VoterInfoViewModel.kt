package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(private val dataSource: ElectionDao, private val division: Division,
                         private val electionId:Int) :
        ViewModel() {

    //TODO: Add live data to hold voter info
    private val _voterResponse = MutableLiveData<VoterInfoResponse>()
    val voterResponse: LiveData<VoterInfoResponse>
        get() = _voterResponse

    init {

        getNetworkVoterInfo()
    }


  private fun  getNetworkVoterInfo(){

      viewModelScope.launch{

          _voterResponse.value = CivicsApi.retrofitService.voterInfoQuery(address, electionId)

          Timber.i("The voter response is${_voterResponse.value }")
      }

  }
    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}