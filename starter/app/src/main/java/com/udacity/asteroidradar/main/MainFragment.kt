package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteroidRepository

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        //defines AsteroidAdapter for the xml as well as setting detailClick to the choosen asteroid
        val adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener
        {
            //this is the lambda expression that onClickListener takes as a parameter; "it" refers to "listener"
            viewModel.detailClick(it)}
        )

        binding.asteroidRecycler.adapter = adapter

        viewModel.menuItemSelected.observe(viewLifecycleOwner, Observer {

            if (it.equals("Today"))
            {
              viewModel.setMasterToSaved(viewModel.domainAsteroidTodayList)
                //viewModel.masterList.value = viewModel.domainAsteroidTodayList.value
            }
            else if (it.equals("Saved"))
            {
                //Log.i("MainSavedList",viewModel.domainAsteroidSavedList.value?.size.toString())
                viewModel.setMasterToSaved(viewModel.domainAsteroidSavedList)
                //viewModel.masterList.value = viewModel.domainAsteroidSavedList.value
            }
            else
                viewModel.setMasterToSaved(viewModel.list)
            //viewModel.masterList.value = viewModel.list.value
        })
        //update the adapter list of asteroids
        //list is a liveData of List<Asteroid> so we can update the adapter by submitting "list"

        //adapter.submitList(viewModel.list.value)
        viewModel.masterMasterList.observe(viewLifecycleOwner, Observer {
               adapter.submitList(it)
        })
        viewModel.detailClick.observe(viewLifecycleOwner, Observer {
            if (it != null)
            {
                //pass in the Asteroid object to safeArgs to be viewed in the detailFragment
               this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.menuItemSelected.value =
            when (item.itemId) {
                R.id.show_today_asteroid -> "Today"
                R.id.show_saved_asteroid -> "Saved"
                else -> "Week"
    }
        return true
    }
}

