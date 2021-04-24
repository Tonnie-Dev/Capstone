package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import timber.log.Timber

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel
    //val viewModel:ElectionsViewModel by viewModels()
private lateinit var viewModel:ElectionsViewModel


    private lateinit var  binding: FragmentElectionBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val db = ElectionDatabase.getInstance(requireContext().applicationContext)

        val factory = ElectionsViewModelFactory(db)

viewModel = ViewModelProvider(this, factory).get(ElectionsViewModel::class.java)
       viewModel.test()

        Timber.i("onCreate()")
        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        binding = FragmentElectionBinding.inflate(inflater)
        return binding.root

    }

    //TODO: Refresh adapters when fragment loads

}