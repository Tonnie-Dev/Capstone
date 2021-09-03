package com.androidshowtime.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidshowtime.database.ElectionDatabase
import com.androidshowtime.network.models.Election
import com.androidshowtime.repo.ElectionsRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

//TODO: Construct ViewModel and provide election datasource


class ElectionsViewModel(val database: ElectionDatabase) : ViewModel() {

    //create repo instance
    private var repo: ElectionsRepo = ElectionsRepo(database)


    // create live data val for upcoming elections - passed to recyclerview via BindingAdapter
    val upComingElections: LiveData<List<Election>>
        get() = repo.upcomingElections


    // Create live data val for followed elections - passed to recyclerview via BindingAdapter
    val followedElections =database.electionDao.getFollowedElections()

    // populate live data for upcoming elections from the API and followed elections

    init {

        viewModelScope.launch {

            //switch to Background
            withContext(IO) {

                repo.refreshDatabaseElections()


            }
        }
    }

    //Create functions to navigate to saved or upcoming election voter info - not needed for now

}