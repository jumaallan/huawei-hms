package com.androidstudy.huaweihms.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.androidstudy.huaweihms.R
import com.androidstudy.huaweihms.data.model.Map
import com.mikhaellopez.circularimageview.CircularImageView
import kotlinx.android.synthetic.main.row_nearby_places.view.*

internal typealias MapClickListener = (Map, Int) -> Unit

object MapDiffer : DiffUtil.ItemCallback<Map>() {
    override fun areItemsTheSame(oldItem: Map, newItem: Map): Boolean {
        return oldItem.formatAddress == newItem.formatAddress
    }

    override fun areContentsTheSame(oldItem: Map, newItem: Map): Boolean {
        return oldItem == newItem
    }
}

@Suppress("DEPRECATION")
internal class NearbyRecyclerViewAdapter(
    private val listener: MapClickListener
) : ListAdapter<Map, NearbyRecyclerViewAdapter.ViewHolder>(MapDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_nearby_places, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewCountryFlag: CircularImageView = itemView.imageViewCountryFlag
        private val textViewFormatAddress: TextView = itemView.textViewFormatAddress
        private val textViewAdminArea: TextView = itemView.textViewAdminArea
        private val textViewSubAdminArea: TextView = itemView.textViewSubAdminArea

        fun bind(map: Map, listener: MapClickListener) {

            imageViewCountryFlag.load("https://www.countryflags.io/${map.countryCode}/flat/48.png")
            textViewFormatAddress.text = map.formatAddress
            textViewAdminArea.text = map.adminArea
            textViewSubAdminArea.text = map.subAdminArea

            itemView.setOnClickListener {
                listener.invoke(map, position)
            }
        }
    }
}
