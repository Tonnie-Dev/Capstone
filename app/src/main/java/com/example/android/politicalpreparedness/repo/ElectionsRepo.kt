package com.example.android.politicalpreparedness.repo

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class ElectionsRepo(private val database: ElectionDatabase) {


    suspend fun getUpcomingElections() {

        withContext(IO) {
            val response = CivicsApi.retrofitService.electionQuery()

        }
    }
}