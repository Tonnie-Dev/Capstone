package com.androidshowtime.election

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.androidshowtime.election.adapter.ElectionListAdapter
import com.androidshowtime.network.models.Election
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("upcomingElectionData")

fun RecyclerView.bindRecyclerViewData(electionData: List<Election>?) {

    //get the ListAdapter

    val adapter = this.adapter as ElectionListAdapter


    /* When this method is called, the ListAdapter diffs the new list
 against the old one and detects items that were added, removed, removed
  or changed. Then the ListAdapter updates the items shown by RecyclerView*/

    adapter.submitList(electionData)


}

@BindingAdapter("followedElectionData")
fun RecyclerView.getFollowedElections(electionData: List<Election>?){

    val adapter = this.adapter as ElectionListAdapter

    adapter.submitList(electionData)
}

@BindingAdapter("dateToString")
fun TextView.dateToStringConverter(election:Election?){

    val simpleDateFormat = SimpleDateFormat("E, MMM dd yyyy",Locale.US)
simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")


    //this.text = simpleDateFormat.format(election.electionDay)

election?.electionDay?.let {
    this.text = simpleDateFormat.format(it)
}

}

@BindingAdapter("noConnectionImageView")

fun ImageView.showErrorImage(showError:Boolean) {


    if (showError ){

        this.visibility = View.VISIBLE
    }
    else{this.visibility = View.GONE}
}