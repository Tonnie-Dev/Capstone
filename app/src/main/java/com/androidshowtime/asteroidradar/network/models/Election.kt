package com.androidshowtime.network.models

import androidx.room.*
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
      @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
)


@Entity(tableName = "followed_elections_table")
data class FollowedElection(@PrimaryKey val id: Int)