package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) : ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(
        ElectionDiffCallback()) {

    //inflate ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {


        return ElectionViewHolder.from(parent)
    }


    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {

        //get election item

        val electionItem = getItem(position)


        //set onClickLister on Holder's itemView and pass in the ElectionListener
        // but omit the brackets for the lambda
        holder.itemView.setOnClickListener { clickListener.onClick(electionItem)}

        //bind electionItem to ViewHolder
        holder.bind(electionItem)


    }

    //TODO: Create ElectionViewHolder
    class ElectionViewHolder(val binding: ElectionItemBinding) : RecyclerView.ViewHolder
    (binding.root) {


        //TODO: Add companion object to inflate ViewHolder (from)

        companion object {

            fun from(parent: ViewGroup): ElectionViewHolder {

                //get LayoutInflater from parent's context
                val inflater = LayoutInflater.from(parent.context)

                //get binding

                val binding = ElectionItemBinding.inflate(inflater)
                return ElectionViewHolder(binding)
            }
        }


        fun bind(election: Election?) {

            binding.election = election
            binding.executePendingBindings()

        }

    }


}

//TODO: Create ElectionDiffCallback

class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

}


//TODO: Create ElectionListener


class ElectionListener(private val onClickListener: (Election) -> Unit) {

    fun onClick(election: Election) = onClickListener(election)

}