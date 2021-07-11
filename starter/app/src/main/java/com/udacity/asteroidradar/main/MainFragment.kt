package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
        binding.asteroidRecycler.adapter = AsteroidAdapter(AsteroidAdapter.onClickListener
        { viewModel.detailClick(it) }
        )


        //update the adapter list of asteroids
        //list is a liveData of List<Asteroid> so we can update the adapter by submiting "list"
        viewModel.list.observe(viewLifecycleOwner, Observer {
            AsteroidAdapter.submitList(it)
            /*asteroid.apply {
                AsteroidAdapter?.videos = videos
            }*/
        }
            )
        viewModel.detailClick.observe(viewLifecycleOwner, Observer {
            if (it != null)
            {
                //pass in the Asteroid object to safeArgs to be viewed in the detailFragment
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
            }
        })



        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
}
