package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {


    @Query("SELECT * FROM election_table WHERE id =:key")
    fun getElectionById(key: Int): Election


    /*insert a list of elections fetched from the network into the database. If and
    election entry is present in database then it is overwritten using REPLACE Strategy*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElections(elections: List<Election>)


    @Query("SELECT * FROM election_table ORDER BY electionDay")
    fun getAllSavedElections(): LiveData<List<Election>>


    

    //TODO: Add delete query
    @Query("DELETE FROM election_table WHERE id = :id")
    fun deleteElections(id: Int)


    //TODO: Add clear query
    @Query("DELETE FROM election_table")
    fun clearElections()

}