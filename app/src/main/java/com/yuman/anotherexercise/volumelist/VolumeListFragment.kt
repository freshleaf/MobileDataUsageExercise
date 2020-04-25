package com.yuman.anotherexercise.volumelist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuman.anotherexercise.R
import kotlinx.android.synthetic.main.fragment_volume_list.*
import kotlinx.android.synthetic.main.switch_item.view.*

/**
 * A simple [Fragment] subclass.
 */
class VolumeListFragment : Fragment() {
    private val volumeListViewModel by activityViewModels<VolumeListViewModel>()
    private lateinit var switchMenuItem: MenuItem
    private val volumeListAdapter = VolumeListAdatper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_volume_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        switchMenuItem = menu.findItem(R.id.app_bar_switch)
        switchMenuItem.actionView.switchCardView.setOnCheckedChangeListener { _, _ ->
            // TODO add switch card view function
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tempMenuItem -> {
                // TODO change to real menu action
                findNavController().navigate(R.id.action_volumeListFragment_to_volumeDetailsFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        volumeListViewModel.volumeList.observe(viewLifecycleOwner, Observer {
            volumeListAdapter.submitList(it)
        })

        recycleView.apply {
            adapter = volumeListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        volumeListViewModel.volumeList.value.isNullOrEmpty().let {
            volumeListViewModel.fetchData()
        }
    }

}
