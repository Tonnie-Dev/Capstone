package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val args:VoterInfoFragmentArgs by navArgs()

    private lateinit var viewModel:VoterInfoViewModel

    private lateinit var binding:FragmentVoterInfoBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentVoterInfoBinding.inflate(inflater)
        //TODO: Add ViewModel values and create ViewModel
        val database = ElectionDatabase.getInstance(requireContext())
        val factory = VoterInfoViewModelFactory(database.electionDao,args.argDivision,args
                .argElectionId)
        viewModel = ViewModelProvider(this, factory).get(VoterInfoViewModel::class.java)

        //TODO: Add binding values

        //make binding observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        //link binding's viewModel to ViewModel
        binding.viewModel = viewModel
        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */



//viewModel.election.observe(viewLifecycleOwner){}
        //TODO: Handle loading of URLs
        viewModel.votingLocationURL.observe(viewLifecycleOwner){ url ->




            binding.stateLocations.setOnClickListener {

                loadUrl(url)
            }
        }

        viewModel.ballotInfoURL.observe(viewLifecycleOwner){ url ->


            binding.stateBallot.setOnClickListener {

                loadUrl(url)
            }

        }
        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        return binding.root
    }

    //TODO: Create method to load URL intents

    private fun loadUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}