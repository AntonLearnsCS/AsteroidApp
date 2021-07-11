package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.GridViewItemBinding

//You must specify DiffCallBack when extending ListAdapter, DiffCallBack is a callback on each item in the viewHolder
//Recall that ListAdapter utilizes DiffUtil (used to determine difference between two lists) in a background thread
class AsteroidAdapter(val listener : OnClickListener) : ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallback) {

    class AsteroidViewHolder(private var binding: GridViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }
    //the benefit of using ListAdapter is that it utilizes DiffCallBack, which allows us to avoid
    //using the computationally expensive "NotifyDataSetChange()", which would rebind and redraw every item and viewHolder
    //respectively, even if the item in the list is not currently being displayed
    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        //returns an inflated viewHolder by passing in a new GridViewItemBinding object into the viewHolder
       return AsteroidViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
        }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        //Q: Where is "getItem" getting the item from? From which data structure is it returning an Asteroid object from?
        //A: Since we are extending listAdapter, listAdapter will generate a list of Asteroid objects for us in the background
        //See exercise7 on SleepTrackerWithRecyclerView lesson
        val asteroidItem = getItem(position)
        holder.bind(asteroidItem)
        holder.itemView.setOnClickListener{
            listener.onClick(asteroidItem)
        }
    }
    //Note: The paramter of onClickListener is a lambda expression that returns a Unit value
    class OnClickListener(val listener : (asteroid : Asteroid) -> Unit) {
        fun onClick(asteroid : Asteroid) = listener(asteroid)
    }


    /*
        class OnClickListener(val clickListener: (marsProperty:MarsProperty) -> Unit) {
        fun onClick(marsProperty:MarsProperty) = clickListener(marsProperty)
    }
     */

}
