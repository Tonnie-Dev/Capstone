package com.androidshowtime.representative.model

import com.androidshowtime.network.models.Office
import com.androidshowtime.network.models.Official

data class Representative (
        val official: Official,
        val office: Office
)