package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import timber.log.Timber

class ElectionsFragment : Fragment() {

    //TODO: Declare ViewModel
    //val viewModel:ElectionsViewModel by viewModels()
    private lateinit var viewModel: ElectionsViewModel


    private lateinit var binding: FragmentElectionBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        //TODO: Add ViewModel values and create ViewModel
        val db = ElectionDatabase.getInstance(requireContext().applicationContext)

        val factory = ElectionsViewModelFactory(db)

        viewModel = ViewModelProvider(this, factory).get(ElectionsViewModel::class.java)
        viewModel.test()

        //TODO: Add binding values
        binding = FragmentElectionBinding.inflate(inflater)
        //make binding observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        //set upcoming recyclerview Adapter
binding.upcomingRecycler.adapter = ElectionListAdapter (ElectionListener{})
        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        binding = FragmentElectionBinding.inflate(inflater)
        return binding.root

    }

    //TODO: Refresh adapters when fragment loads

}