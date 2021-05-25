package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.util.Linkify
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    private val args: VoterInfoFragmentArgs by navArgs()

    private lateinit var viewModel: VoterInfoViewModel


    private lateinit var binding: FragmentVoterInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //rename action bar
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.voter_info_fragment)

        binding = FragmentVoterInfoBinding.inflate(inflater)

        val database = ElectionDatabase.getInstance(requireContext())
        val factory = VoterInfoViewModelFactory(
            database.electionDao, args.argDivision, args
                .argElectionId
        )
        viewModel = ViewModelProvider(this, factory).get(VoterInfoViewModel::class.java)


        //make binding observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        //link binding's viewModel to ViewModel
        binding.viewModel = viewModel


        viewModel.votingLocationURL.observe(viewLifecycleOwner) { url ->

            loadUrl(url)
        }

        viewModel.ballotInfoURL.observe(viewLifecycleOwner) { url ->

            loadUrl(url)
        }

        // Handle save button UI state

        viewModel.isElectionFollowed.observe(viewLifecycleOwner) { isFollowed ->

            //set text to 'Unfollow Election'
            if (isFollowed) {

                binding.followElectionButton.text = getString(R.string.unfollow_election_text)

            }

            //set text to 'Follow Election'
            else {

                binding.followElectionButton.text = getString(R.string.follow_election_text)
            }

        }

        //Handle save button clicks
        binding.followElectionButton.setOnClickListener {

            viewModel.onClickFollowElectionButton()
        }

        //observe for connection

        viewModel.noConnection.observe(viewLifecycleOwner) {

                noInternet ->
            if (noInternet) {

                val msg = getString(R.string.no_connection_string)
                showSnackBar(msg)
                binding.electionDate.visibility = View.GONE

                binding.stateHeader.text = getString(R.string.error)
                binding.stateLocations.visibility = View.GONE
                binding.stateBallot.visibility = View.GONE
                binding.followElectionButton.visibility = View.GONE
            }

        }

        viewModel.isSiteInvalid.observe(viewLifecycleOwner) {

                notValidURL ->
            if (notValidURL) {

                val msg = getString(R.string.invalid_url_msg)
                showSnackBar(msg)
            }
        }

        return binding.root
    }

    // Create method to load URL intents

    private fun loadUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


    private fun showSnackBar(message: String) {

        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

}

