package com.androidshowtime.repo

import com.androidshowtime.database.ElectionDatabase
import com.androidshowtime.network.CivicsApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionsRepo(private val database: ElectionDatabase) {

//GET ELECTIONS FROM NETWORK INTO DATABASE
    suspend fun refreshDatabaseElections() {

        withContext(IO) {


            try {
                //get elections from Network
                val response = CivicsApi.retrofitService.electionQuery()
                val elections = response.elections


                //insert elections into database
                database.electionDao.insertElections(elections)
            }

            catch (e:Exception) {

e.printStackTrace()
            }

        }
    }


//EXPOSE THE DATABASE ELECTIONS AS LIVEDATA
val upcomingElections = database.electionDao.getAllElections()

}