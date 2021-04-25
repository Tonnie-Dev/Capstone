package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse

class DataTransferObjects {

    data class NetworkElectionsContainer(val electionResponse: List<ElectionResponse>)


    fun NetworkElectionsContainer.asDatabaseModel() {}




}