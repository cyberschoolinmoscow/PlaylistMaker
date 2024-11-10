package com.practicum.playlistmaker.presentation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.TrackPreferences
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.models.Track

class SearchActivity : AppCompatActivity() {
    private val tracks = ArrayList<Track>()

    private val trackAdapter: TrackAdapter = TrackAdapter(tracks, this)
    private val historyList: ArrayList<Track> = ArrayList()
    private val historyAdapter = TrackAdapter(historyList, this)

    private lateinit var viewBinding: ActivitySearchBinding

    companion object {
        const val INPUT_STRING = "input"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.ivClear.setOnClickListener {
            viewBinding.etSearch.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(viewBinding.etSearch.windowToken, 0)
            historyList.clear()
            historyList.addAll(TrackPreferences.read(App.sharedPreferences))
            showHistory()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    viewBinding.buttonClearHistory.isVisible = false
                    viewBinding.tvHistory.isVisible = false
                    viewBinding.trackList.adapter = null
                    val input = s.toString()

                    savedInstanceState?.putString(INPUT_STRING, input)

                    searchDebounce()
                } else {
                    showHistory()
                }
                viewBinding.ivClear.isVisible = clearButtonVisibility(s)
                viewBinding.placeholderLayout.isVisible = false
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        viewBinding.etSearch.addTextChangedListener(simpleTextWatcher)

        val myToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.idSearchToolbar)
        setSupportActionBar(myToolbar)
        myToolbar.setNavigationOnClickListener { finish() }

        viewBinding.trackList.layoutManager = LinearLayoutManager(this)
        historyList.addAll(TrackPreferences.read(App.sharedPreferences))

        viewBinding.buttonUpdate.setOnClickListener {
            searchRequest()
        }
        viewBinding.buttonClearHistory.setOnClickListener {
            TrackPreferences.removeAll()
            historyList.clear()
            showHistory()
        }
        App.sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, _ ->
            historyList.clear()
            historyList.addAll(TrackPreferences.read(sharedPreferences))
            historyAdapter.updateTracks(historyList)
        }
        viewBinding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRequest()
                true
            }
            false
        }
        viewBinding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && viewBinding.etSearch.text.isEmpty()) {
                showHistory()
            }
        }
    }

    private fun showHistory() {
        viewBinding.buttonClearHistory.isVisible = historyList.size > 0
        viewBinding.tvHistory.isVisible = historyList.size > 0
        viewBinding.trackList.adapter = historyAdapter
        historyAdapter.updateTracks(historyList)
    }

    private fun searchRequest() {
        if (viewBinding.etSearch.text.isNotEmpty()) {
            viewBinding.placeholderLayout.isVisible = false
            viewBinding.progressBar.isVisible = true
            var queryStatus: QueryStatus = QueryStatus.WAITING

            val tracksInteractor = Creator.provideTracksInteractor()
            tracksInteractor.searchTracks(viewBinding.etSearch.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        handler.post {
                            if (foundTracks != null) {
                                tracks.clear()
                                tracks.addAll(foundTracks)
                                viewBinding.trackList.adapter = trackAdapter
                                trackAdapter.updateTracks(tracks)
                            }
                            if (foundTracks == null) {
                                queryStatus = QueryStatus.NO_INTERNET
                            } else if (foundTracks.isEmpty()) {
                                queryStatus = QueryStatus.NOT_FOUND
                            } else {
                                tracks.addAll(foundTracks)
                                viewBinding.trackList.adapter = trackAdapter
                                trackAdapter.updateTracks(tracks)
                            }
                        }

                    }
                }
            )
            viewBinding.progressBar.isVisible = false
            showMessage(queryStatus)
        } else {
            viewBinding.trackList.adapter = historyAdapter
        }
    }

    private fun showMessage(queryStatus: QueryStatus) {
        if (queryStatus == QueryStatus.SUCCESS) {
            viewBinding.buttonClearHistory.isVisible = false
            viewBinding.tvHistory.isVisible = false
            return
        }
        if (queryStatus == QueryStatus.WAITING) {
            viewBinding.buttonClearHistory.isVisible = false
            viewBinding.tvHistory.isVisible = false
            return
        }
        if (queryStatus == QueryStatus.NOT_FOUND || queryStatus == QueryStatus.NO_INTERNET) {
            viewBinding.placeholderLayout.isVisible = true
            viewBinding.buttonUpdate.isVisible = queryStatus.visibility
            tracks.clear()
            trackAdapter.updateTracks(tracks)
            viewBinding.placeholderMessage.text = getString(queryStatus.message)
            viewBinding.placeholderMessage.setCompoundDrawablesWithIntrinsicBounds(
                0,
                queryStatus.drawable,
                0,
                0
            )
            return
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewBinding.etSearch.setText(savedInstanceState.getString(INPUT_STRING))
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    enum class QueryStatus(val message: Int, val drawable: Int, val visibility: Boolean) {
        NOT_FOUND(R.string.nothing_found, R.drawable.nothing, false),
        NO_INTERNET(R.string.something_went_wrong, R.drawable.internet, true),
        WAITING(-1, 0, false),
        SUCCESS(-1, 0, false)
    }

    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

    }
}

