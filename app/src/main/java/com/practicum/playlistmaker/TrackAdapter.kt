package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private var tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_cardview, parent, false)
        return TrackViewHolder(view)
    }

    lateinit var contex: Context
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        val itemClickListener: OnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(item: Track) {
                TrackPreferences.writeTrack(item)

                contex.startActivity(
                    Intent(
                        contex,
                        AudioPlayerActivity::class.java
                    ).putExtra("track", item.serializeTrack())
                )

            }
        }
        holder.bind(tracks[position], itemClickListener)
    }

    override fun getItemCount() = tracks.size

    fun updateTracks(newTracks: List<Track>) {
        val oldTracks = tracks
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldTracks.size
            }

            override fun getNewListSize(): Int {
                return newTracks.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldTracks[oldItemPosition].trackId == newTracks[newItemPosition].trackId
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldTracks[oldItemPosition].trackId == newTracks[newItemPosition].trackId
            }

        })
        tracks = newTracks.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }
}