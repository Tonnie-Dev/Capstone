package com.androidshowtime.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidshowtime.database.ElectionDao
import com.androidshowtime.network.CivicsApi
import com.androidshowtime.network.models.Division
import com.androidshowtime.network.models.Election
import com.androidshowtime.network.models.FollowedElection
import com.androidshowtime.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException


class VoterInfoViewModel(
    private val dao: ElectionDao, private val division: Division,
    private val electionId: Int
) :
    ViewModel() {


    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private val _votingLocationURL = MutableLiveData<String>()
    val votingLocationURL: LiveData<String>
        get() = _votingLocationURL

    private val _ballotInfoURL = MutableLiveData<String>()
    val ballotInfoURL: LiveData<String>
        get() = _ballotInfoURL


    private val _isElectionFollowed = MutableLiveData(false)
    val isElectionFollowed: LiveData<Boolean>
        get() = _isElectionFollowed

    private val _noConnection = MutableLiveData<Boolean>(false)
    val noConnection: LiveData<Boolean>
        get() = _noConnection

    private var buttonModeFollow: Boolean? = null

    private val _isSiteInvalid = MutableLiveData<Boolean>(false)
    val isSiteInvalid: LiveData<Boolean>
        get() = _isSiteInvalid

    init {

        getNetworkVoterInfo()
        getElectionFromDatabase(electionId)
        checkIdExistsInDatabase()
        buttonModeFollow = _isElectionFollowed.value
    }

    private fun getElectionFromDatabase(id: Int) {
        viewModelScope.launch {

            withContext(IO) {
                //Access Database on IO Coroutine Scope
                _election.postValue(dao.getElectionById(id))
            }

        }
    }

    private fun getNetworkVoterInfo() {

        val address = getAddressFromDivision(division)
        viewModelScope.launch {

            withContext(IO) {
                try {

                    _voterInfoResponse.postValue(
                        CivicsApi.retrofitService.voterInfoQuery(
                            address,
                            electionId
                        )
                    )
                } catch (e: UnknownHostException) {

                    _noConnection.postValue(true)

                } catch (e: HttpException) {


                }


            }

        }
    }


    private fun getAddressFromDivision(division: Division): String {
        var address = ""

        address = if (division.state.isBlank() || division.state.isEmpty()) {
            "country:/state:ny"
        } else {
            "country:/state:${division.state}"
        }


        return address
    }


    fun onVotingLocationLinkClick() {

        try {
            _votingLocationURL.value =
                _voterInfoResponse.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl!!
        } catch (e: NullPointerException) {

            _isSiteInvalid.value = true

        }

    }

    fun onBallotInfoLinkClick() {

        try {
            _ballotInfoURL.value =
                _voterInfoResponse.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl!!


        } catch (e: NullPointerException) {

            _isSiteInvalid.value = true
        }


    }
    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    fun onClickFollowElectionButton() {


        if (_isElectionFollowed.value == true) {


            unfollowElection(electionId)


        } else {

            val election = _election.value

            election?.let {

                followElection(it)
                // buttonModeFollow = false
            }


        }

    }

    private fun followElection(election: Election) {
        _isElectionFollowed.value = true

        viewModelScope.launch {

            withContext(IO) {

                dao.insertFollowedElection(FollowedElection(election.id))
                //   _isElectionFollowed.postValue(true)
            }

        }

    }

    private fun unfollowElection(id: Int) {

        _isElectionFollowed.value = false

        viewModelScope.launch {

            withContext(IO) {
                dao.deleteElections(id)
                //_isElectionFollowed.postValue(false)

            }

        }

    }


    private fun checkIdExistsInDatabase() {

        viewModelScope.launch {

            withContext(IO) {

                _isElectionFollowed.postValue(dao.isElectionFollowed(electionId))

            }

        }


    }


}