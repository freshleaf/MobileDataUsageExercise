package com.yuman.anotherexercise.volumelist

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.yuman.anotherexercise.R
import com.yuman.anotherexercise.VolumeApplication
import com.yuman.anotherexercise.data.VolumeRepository
import com.yuman.anotherexercise.data.local.VolumeDatabase
import com.yuman.anotherexercise.data.remote.RemoteDataSource
import com.yuman.anotherexercise.util.FetchDataStatus
import kotlinx.android.synthetic.main.fragment_volume_list.*
import kotlinx.android.synthetic.main.switch_item.view.*

/**
 * the "list screen", show list of every year data usage volume
 * support normal list and card list
 */
class VolumeListFragment : Fragment() {
    private val volumeListViewModel by viewModels<VolumeListViewModel> {
        VolumeViewModelFactory((requireContext().applicationContext as VolumeApplication).volumeRepository)
    }

    private lateinit var switchMenuItem: MenuItem
    private val volumeListAdapter = VolumeListAdatper(false)
    private val volumeListAdapterCard = VolumeListAdatper(true)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_volume_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        switchMenuItem = menu.findItem(R.id.app_bar_switch)
        switchMenuItem.actionView.switchCardView.setOnCheckedChangeListener { _, b: Boolean ->
            if (b) {
                recycleView.adapter = volumeListAdapterCard
                volumeListViewModel.isCardView = true
            } else {
                recycleView.adapter = volumeListAdapter
                volumeListViewModel.isCardView = false
            }
            volumeListViewModel.updateIsCardView(b)
        }
        switchMenuItem.actionView.switchCardView.isChecked = volumeListViewModel.isCardView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_swipe -> {
                swipeLayout.isRefreshing = true
                // let the progressbar show
                Handler().postDelayed({ volumeListViewModel.resetQuery() }, 1000)
            }
            R.id.menu_item_clear_cache -> volumeListViewModel.clearLocalCache()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        volumeListViewModel.volumeList.observe(viewLifecycleOwner, Observer {
            volumeListAdapter.submitList(it)
            volumeListAdapterCard.submitList(it)
            swipeLayout.isRefreshing = false
        })
        volumeListViewModel.fetchDataStatus.observe(viewLifecycleOwner, Observer {
            swipeLayout.isRefreshing = false
            if (it == FetchDataStatus.NETWORK_ERROR || it == FetchDataStatus.NETWORK_NOT_SUCCESS) {
                Snackbar.make(
                    requireActivity().volumeListFragmentView,
                    getString(R.string.msg_fetch_data_failed),
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.button_ok) {}
                    .show()
            }
        })

        recycleView.apply {
            adapter = if (volumeListViewModel.isCardView) {
                volumeListAdapterCard
            } else {
                volumeListAdapter
            }
            layoutManager = LinearLayoutManager(requireContext())
        }

        if (volumeListViewModel.volumeList.value.isNullOrEmpty()) {
            swipeLayout.isRefreshing = true
            volumeListViewModel.resetQuery()
        }

        swipeLayout.setOnRefreshListener {
            volumeListViewModel.fetchData()
        }
    }

}
