package com.example.android.politicalpreparedness.repo

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionsRepo(private val database: ElectionDatabase) {


    suspend fun getUpcomingElections() {

        withContext(IO) {
            val response = CivicsApi.retrofitService.electionQuery()

            Timber.i("The response:$response")

        }
    }



    suspend fun getVoterInfo() {

        withContext(IO) {
            /*val response = CivicsApi.retrofitService.voterInfoQuery()

            Timber.i("The response:$response")*/

        }
    }

    suspend fun getRepInfo() {

        withContext(IO) {
            /*val response = CivicsApi.retrofitService.representativeInfoByAddress()

            Timber.i("The response:$response")*/

        }
    }



}