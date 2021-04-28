package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class VoterInfoViewModel(private val dataSource: ElectionDao, private val division: Division,
                         private val electionId: Int) :
        ViewModel() {

    //TODO: Add live data to hold voter info

    private var electionVariable: Election? = null

    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private var _votingLocationURL = MutableLiveData<String>()
    val votingLocationURL: LiveData<String>
        get() = _votingLocationURL

    private val _ballotInfoURL = MutableLiveData<String>()
    val ballotInfoURL: LiveData<String>
        get() = _ballotInfoURL


    init {

        getNetworkVoterInfo()
        getElectionFromDatabase(electionId)

    }


    private fun getElectionFromDatabase(id: Int) {
        viewModelScope.launch {

            withContext(IO) {
                //Access Database on IO Coroutine Scope
                _election.postValue(dataSource.getElectionById(id))
            }


        }

    }


    private fun getNetworkVoterInfo() {

        val address = getAddressFromDivision(division)
        viewModelScope.launch {

            _voterInfoResponse.value = CivicsApi.retrofitService.voterInfoQuery(address, electionId)

            Timber.i("The voter response is${_voterInfoResponse.value}")
        }
    }


    private fun getAddressFromDivision(division: Division): String {
        var address = ""

        address = if (division.state.isBlank() || division.state.isEmpty()) {
            "country:/state:ny"
        } else {
            "country:/state:${division.state}"
        }


        Timber.i("The Address is $address")
        return address

    }


    fun onVotingLocationLinkClick() {

        _votingLocationURL.value = _voterInfoResponse.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl!!
    }

    fun onBallotInfoLinkClick() {

        _ballotInfoURL.value = _voterInfoResponse.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl!!


    }
    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}