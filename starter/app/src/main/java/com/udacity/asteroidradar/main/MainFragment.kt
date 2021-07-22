package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

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

        //TODO: How can I improve this filter, as it seems convoluted to have three seperate obervers for each list
        viewModel.menuItemSelected.observe(viewLifecycleOwner, Observer {
            //viewModel.masterList = viewModel.list
            if (it == "Week")
            {
                //Log.i("MainFragment",viewModel.weekList.toString())
                //viewModel._masterList.value = viewModel.weekList.value
                viewModel.weekList.observe(viewLifecycleOwner, Observer {
                    viewModel._masterList.value = it
                })
                viewModel.weekList.value?.get(0)?.closeApproachDate?.let { it1 ->
                    Log.i("MainFragmen1",
                        it1
                    )
                }
            }
            //Note: You can't set a LiveData to a MutableLiveData, encapsulation only lets you set a
            else if (it.equals("Saved"))
            {
                viewModel.domainAsteroidSavedList.observe(viewLifecycleOwner, Observer {
                    viewModel._masterList.value = it
                })
                //viewModel._masterList.value = viewModel.domainAsteroidSavedList.value
            }
            else
            {
                viewModel.domainAsteroidTodayList.observe(viewLifecycleOwner, Observer {
                    viewModel._masterList.value = it
                })
            }
        })

        viewModel.masterList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.detailClick.observe(viewLifecycleOwner, Observer {
            if (it != null)
            {
                //pass in the Asteroid object to safeArgs to be viewed in the detailFragment
               this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.completeClick()
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

