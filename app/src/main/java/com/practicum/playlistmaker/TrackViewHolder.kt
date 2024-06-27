package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val artistImage: ImageView = itemView.findViewById(R.id.artistImage)

    fun bind(model: Track, onItemClickListener: OnItemClickListener) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.getDuration()
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(artistImage)
        itemView.setOnClickListener {
            onItemClickListener.onItemClick(model)
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(item: Track)
}