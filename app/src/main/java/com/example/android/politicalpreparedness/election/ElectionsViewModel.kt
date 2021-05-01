package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.repo.ElectionsRepo
import kotlinx.coroutines.launch
import timber.log.Timber

//TODO: Construct ViewModel and provide election datasource




  class ElectionsViewModel(val database:ElectionDatabase): ViewModel() {


  private lateinit var repo: ElectionsRepo
init {
  repo = ElectionsRepo(database)

  test()
    Timber.i("Init ViewModel Called")
}


     fun  test(){

         Timber.i("test Called")
      viewModelScope.launch {

          Timber.i("inside viewModelScope")
        repo.getRepInfo()
        repo.getUpcomingElections()
        repo.getVoterInfo()
      }
    }



    //TODO: Create live data val for upcoming elections
val upComingElections = repo.savedElections


    //TODO: Create live data val for saved elections



    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}