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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollowedElection( followedElection: Election)

    @Query("SELECT * FROM election_table WHERE id =:key")
    fun getElectionById(key: Int): Election

    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    fun getAllSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id in (SELECT * FROM followed_elections_table) ")
    fun getFollowedElections(): LiveData<List<Election>>

    @Query("DELETE FROM followed_elections_table WHERE id = :id")
    fun deleteElections(id: Int)

    @Query("DELETE FROM election_table")
    fun clearElections()

}