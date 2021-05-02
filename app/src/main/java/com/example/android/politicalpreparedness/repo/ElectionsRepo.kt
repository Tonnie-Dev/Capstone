package com.example.android.politicalpreparedness.repo

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionsRepo(private val database: ElectionDatabase) {

val savedElections = database.electionDao.getAllElections()
    suspend fun getUpcomingElections() {

        withContext(IO) {
            val response = CivicsApi.retrofitService.electionQuery()
            val elections = response.elections

            Timber.i("Election Query response is:$elections")

            //insert elections into database
            database.electionDao.insertElections(elections)
        }
    }






}