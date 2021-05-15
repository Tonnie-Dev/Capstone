package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class LaunchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //rename the actionBar
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.launch_fragment_string)
        val binding = FragmentLaunchBinding.inflate(inflater)

        //make binding observe LiveData
        binding.lifecycleOwner = this

        //onClick Listeners
        binding.findRepsButton.setOnClickListener { navToRepresentatives() }
        binding.upcomingElectionsButton.setOnClickListener { navToElections() }

        return binding.root
    }

    private fun navToElections() {
        this.findNavController().navigate(LaunchFragmentDirections
                                              .actionLaunchFragmentToElectionsFragment())
    }

    private fun navToRepresentatives() {
     findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
    }

}
