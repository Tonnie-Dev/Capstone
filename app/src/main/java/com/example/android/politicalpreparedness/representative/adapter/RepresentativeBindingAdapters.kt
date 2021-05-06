package com.example.android.politicalpreparedness.representative.adapter

import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.representative.model.Representative
import timber.log.Timber

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        //TODO: Add Glide call to load image and circle crop - user ic_profile as a placeholder and for errors.
    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T>{
    return adapter as ArrayAdapter<T>
}


@BindingAdapter("repsRecyclerViewData")

fun RecyclerView.populateRecyclerView(reps: List<Representative>?){


    val adapter = this.adapter as RepresentativeListAdapter
    adapter.submitList(reps)
}


@BindingAdapter("loadImageFromCoil")
fun ImageView.getImageFromCoil(imageUrl:String?){
    imageUrl?.let {

      val uri =  it.toUri().buildUpon().scheme("https").build()

        //Glide.with(this.context).load(uri)
       load(uri){
           crossfade(750)
           transformations(CircleCropTransformation())
           error(R.drawable.ic_broken_image)
           placeholder(R.drawable.avatar)

       }
    }


}
