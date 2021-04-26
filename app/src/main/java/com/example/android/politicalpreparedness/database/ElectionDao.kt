package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    /*insert a list of elections fetched from the network into the database. If and
    election entry is present in database then it is overwritten using REPLACE Strategy*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElections(elections: List<Election>)


    //TODO: Add select all election query

    @Query("SELECT * FROM election_table ORDER BY electionDay")
    fun getAllSavedElections(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("SELECT * FROM election_table WHERE id=:key")
    fun getElectionById(key: Int)
    //TODO: Add delete query

    //TODO: Add clear query

}