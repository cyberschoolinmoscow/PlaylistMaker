package com.practicum.playlistmaker.presentation.tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track

class TrackAdapter(
    var tracks: List<Track>,
    var contex: SearchActivity,
    val onTrackClick: OnTrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {
    interface OnTrackClickListener {
        fun onTrackClick(track: Track)

    }

//    companion object {
//        private const val CLICK_DEBOUNCE_DELAY = 1000L
//    }
//
//    private var isClickAllowed = true
//
//    private val handler = Handler(Looper.getMainLooper())
//
//    private fun clickDebounce(): Boolean {
//        val current = isClickAllowed
//        if (isClickAllowed) {
//            isClickAllowed = false
//            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
//        }
//        return current
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_cardview, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

//        val itemClickListener: OnItemClickListener = object : OnItemClickListener {
//            override fun onItemClick(item: Track) {
//                if (clickDebounce()) {
//                    TrackPreferences.writeTrack(item)
//                    contex.startActivity(
//                        Intent(
//                            contex,
//                            AudioPlayerActivity::class.java
//                        ).putExtra("track", item.serializeTrack())
//                    )
//                }
//            }
//        }
//        holder.bind(tracks[position], itemClickListener)
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onTrackClick.onTrackClick(tracks[position]) }
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