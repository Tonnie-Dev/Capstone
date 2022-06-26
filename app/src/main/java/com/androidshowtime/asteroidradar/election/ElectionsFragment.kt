package com.androidshowtime.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidshowtime.R
import com.androidshowtime.database.ElectionDatabase
import com.androidshowtime.databinding.FragmentElectionBinding
import com.androidshowtime.election.adapter.ElectionListAdapter
import com.androidshowtime.election.adapter.ElectionListener
import timber.log.Timber

class ElectionsFragment : Fragment() {


  

    //TODO: Declare ViewModel
    //val viewModel:ElectionsViewModel by viewModels()
    private lateinit var viewModel: ElectionsViewModel


    private lateinit var binding: FragmentElectionBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {




        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.elections_fragment_string)
        //TODO: Add ViewModel values and create ViewModel
        val db = ElectionDatabase.getInstance(requireContext())

        val factory = ElectionsViewModelFactory(db)

        viewModel = ViewModelProvider(this, factory).get(ElectionsViewModel::class.java)
        //viewModel.test()

        //TODO: Add binding values

        binding = FragmentElectionBinding.inflate(inflater)

        //make binding observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        //link binding's ViewModel to the ViewModel class
        binding.viewModel = viewModel

        //set upcoming recyclerview Adapter
        binding.upcomingRecycler.adapter = ElectionListAdapter(ElectionListener {

       findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment
       (it.id, it.division))
            Timber.i("there id transmitted is ${it.id} and Division is ${it.division}")

        })


        //set followed recyclerview Adapter
        binding.savedRecycler.adapter = ElectionListAdapter(ElectionListener {

            findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment
            (it.id, it.division))
        })

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters


        return binding.root

    }

    //TODO: Refresh adapters when fragment loads

}