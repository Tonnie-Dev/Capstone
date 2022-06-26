package com.androidshowtime.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidshowtime.database.ElectionDatabase

//TODO: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(private val db: ElectionDatabase): ViewModelProvider
.NewInstanceFactory
() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ElectionsViewModel(db) as T
    }
}