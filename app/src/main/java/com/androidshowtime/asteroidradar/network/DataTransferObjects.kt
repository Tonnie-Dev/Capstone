package com.androidshowtime.network

import com.androidshowtime.network.models.Election
import com.androidshowtime.network.models.ElectionResponse

class DataTransferObjects {

    data class NetworkElectionsContainer(val electionResponse: List<ElectionResponse>)


    fun NetworkElectionsContainer.asDatabaseModel() {}




}