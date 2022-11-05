package com.androidshowtime.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androidshowtime.network.models.Election
import com.androidshowtime.network.models.FollowedElection

@Dao
interface ElectionDao {


    /*insert a list of elections fetched from the network into the database. If and
    election entry is present in database then it is overwritten using REPLACE Strategy*/


    //FOLLOWED_ELECTION_QUERIES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollowedElection(followedElection: FollowedElection)

    @Query("SELECT EXISTS (SELECT 1 FROM followed_elections_table WHERE id = :id)")
    fun isElectionFollowed(id: Int):Boolean
    @Query("DELETE FROM followed_elections_table WHERE id = :id")
    fun deleteElections(id: Int)

    //ALL_ELECTIONS_QUERIES
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElections(elections: List<Election>)

    @Query("SELECT * FROM election_table WHERE id =:key")
    fun getElectionById(key: Int): Election

    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id in (SELECT * FROM followed_elections_table) ORDER BY name ASC ")
    fun getFollowedElections(): LiveData<List<Election>>

    @Query("DELETE FROM election_table")
    fun clearElections()




}