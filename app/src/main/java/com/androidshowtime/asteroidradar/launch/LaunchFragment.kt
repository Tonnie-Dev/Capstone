package com.androidshowtime.launch

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidshowtime.R
import com.androidshowtime.databinding.FragmentLaunchBinding
import com.androidshowtime.election.adapter.ElectionListAdapter
import com.androidshowtime.election.adapter.ElectionListener

class LaunchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //rename the actionBar
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.launch_fragment_string)
        val binding = FragmentLaunchBinding.inflate(inflater)

        //make binding observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

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
