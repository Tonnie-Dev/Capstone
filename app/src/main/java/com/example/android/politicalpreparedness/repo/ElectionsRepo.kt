package com.example.android.politicalpreparedness.repo

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionsRepo(private val database: ElectionDatabase) {

//GET ELECTIONS FROM NETWORK INTO DATABASE
    suspend fun refreshDatabaseElections() {

        withContext(IO) {

            //get elections from Network
            val response = CivicsApi.retrofitService.electionQuery()
            val elections = response.elections

            Timber.i("The response is $elections")
            //insert elections into database
            database.electionDao.insertElections(elections)
        }
    }


//EXPOSE THE DATABASE ELECTIONS AS LIVEDATA
val upcomingElections = database.electionDao.getAllElections()

}