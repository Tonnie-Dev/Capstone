package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repo.ElectionsRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//TODO: Construct ViewModel and provide election datasource


class ElectionsViewModel(val database: ElectionDatabase) : ViewModel() {

    //create repo instance
    private var repo: ElectionsRepo = ElectionsRepo(database)


    // create live data val for upcoming elections
    val upComingElections: LiveData<List<Election>>
        get() = repo.upcomingElections


    // Create live data val for followed elections
    val followedElections: LiveData<List<Election>>
        get() = database.electionDao.getFollowedElections()

// populate live data for upcoming elections from the API and saved

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