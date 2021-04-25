package com.example.android.politicalpreparedness.election

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("upcomingElectionData")

fun RecyclerView.bindRecyclerViewData(electionData: List<Election>?) {

    //get the ListAdapter

    val adapter = this.adapter as ElectionListAdapter
    Timber.i("Checking The adapter which is $adapter")

    /* When this method is called, the ListAdapter diffs the new list
 against the old one and detects items that were added, removed, removed
  or changed. Then the ListAdapter updates the items shown by RecyclerView*/

    adapter.submitList(electionData)


}

@BindingAdapter("dateToString")
fun TextView.dateToStringConverter(election:Election){

    val simpleDateFormat = SimpleDateFormat("E, MMM dd, 00:mm:ss z yyyy",Locale.US)
simpleDateFormat.timeZone = TimeZone.getTimeZone("ET")
    this.text = simpleDateFormat.format(election.electionDay)



}