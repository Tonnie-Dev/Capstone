package com.androidshowtime.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidshowtime.database.ElectionDao
import com.androidshowtime.network.models.Division

//TODO: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val dataSource: ElectionDao, private val division: Division,
                                private val electionId: Int) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return (VoterInfoViewModel(dataSource, division, electionId) as T)
    }
}