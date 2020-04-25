package com.yuman.anotherexercise.volumelist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yuman.anotherexercise.R
import com.yuman.anotherexercise.data.YearVolumeItem
import kotlinx.android.synthetic.main.volume_cell_normal.view.*
import java.text.NumberFormat

class VolumeListAdatper :
    ListAdapter<YearVolumeItem, VolumeListAdatper.VolumeListViewHolder>(DIFFCALLBACK) {

    private val nf = NumberFormat.getInstance().apply {
        this.maximumFractionDigits = 6
        this.isGroupingUsed = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolumeListViewHolder {
        val viewHolder = VolumeListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.volume_cell_normal, parent, false)
        )
        viewHolder.ivDown.setOnClickListener {
            AlertDialog.Builder(parent.context).also {
                it.setTitle(R.string.dropdown_alert_title).setMessage(R.string.msg_volume_dropdown)
                it.setPositiveButton(R.string.button_ok) { _, _ -> }
                it.create().show()
            }
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VolumeListViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvYear.text = item.year.toString()
        val volume = item.volume
        val volumeStr = nf.format(volume)
        val numbers = volumeStr.split('.')
        var firstPart = "--"
        var secondPart = "--"
        if (numbers.isNotEmpty() && numbers.size <= 2) {
            firstPart = numbers[0]
            if (numbers.size == 2) {
                secondPart = numbers[1]
            }
        }

        holder.tvVolumeInt.text = firstPart
        holder.tvVolumeDecimal.text = ".$secondPart"
        with(item.quarterItems) {
            if (this[0] == null) {
                holder.tvQ1.visibility = View.INVISIBLE
            } else {
                holder.tvQ1.visibility = View.VISIBLE
            }
            if (this[1] == null) {
                holder.tvQ2.visibility = View.INVISIBLE
            } else {
                holder.tvQ2.visibility = View.VISIBLE
            }
            if (this[2] == null) {
                holder.tvQ3.visibility = View.INVISIBLE
            } else {
                holder.tvQ3.visibility = View.VISIBLE
            }
            if (this[3] == null) {
                holder.tvQ4.visibility = View.INVISIBLE
            } else {
                holder.tvQ4.visibility = View.VISIBLE
            }
        }
        if (item.isDropdown) {
            holder.ivDown.visibility = View.VISIBLE
            holder.ivDown.isClickable = true
        } else {
            holder.ivDown.visibility = View.INVISIBLE
            holder.ivDown.isClickable = false
        }
    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<YearVolumeItem>() {
        override fun areItemsTheSame(oldItem: YearVolumeItem, newItem: YearVolumeItem): Boolean {
            return oldItem.year == newItem.year
        }

        override fun areContentsTheSame(oldItem: YearVolumeItem, newItem: YearVolumeItem): Boolean {
            return oldItem == newItem
        }
    }

    class VolumeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvYear: TextView = itemView.tvYear
        val tvVolumeInt: TextView = itemView.tvVolumeInt
        val tvVolumeDecimal: TextView = itemView.tvVolumeDecimal
        val tvQ1: TextView = itemView.tvQ1
        val tvQ2: TextView = itemView.tvQ2
        val tvQ3: TextView = itemView.tvQ3
        val tvQ4: TextView = itemView.tvQ4
        val ivDown: ImageView = itemView.ivDown
    }
}