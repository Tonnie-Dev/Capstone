package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel
    val viewModel:ElectionsViewModel by viewModels()


    private lateinit var  binding: FragmentLaunchBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        binding = FragmentLaunchBinding.inflate(inflater)
        return binding.root

    }

    //TODO: Refresh adapters when fragment loads

}