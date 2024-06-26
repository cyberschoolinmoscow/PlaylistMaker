package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val imdbService = retrofit.create(IMDbApi::class.java)
    private val tracks = ArrayList<Track>()
    private val trackAdapter: TrackAdapter = TrackAdapter(tracks, this)
    private val historyList: ArrayList<Track> = ArrayList()
    private val historyAdapter = TrackAdapter(historyList, this)
    private lateinit var inputEditText: EditText
    private lateinit var placeholderLayout: LinearLayout
    private lateinit var placeholderMessage: MaterialTextView
    private lateinit var buttonUpdate: MaterialButton
    private lateinit var buttonClearHistory: MaterialButton
    private lateinit var tvHistory: AppCompatTextView
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        inputEditText = findViewById(R.id.et_search)
        placeholderLayout = findViewById(R.id.placeholder_layout)
        placeholderMessage = findViewById(R.id.placeholder_message)
        buttonUpdate = findViewById(R.id.button_update)
        val clearButton: ImageView = findViewById(R.id.iv_clear)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
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
                    val input = s.toString()

                    savedInstanceState?.putString(INPUT_STRING, input)
                }
                clearButton.isVisible = clearButtonVisibility(s)
                placeholderLayout.isVisible = false
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        val myToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.idSearchToolbar)
        setSupportActionBar(myToolbar)
        myToolbar.setNavigationOnClickListener { finish() }

        recycler = findViewById(R.id.trackList)
        recycler.layoutManager = LinearLayoutManager(this)
        historyList.addAll(TrackPreferences.read(App.sharedPreferences))

        buttonUpdate.setOnClickListener {
            searchRequest()
        }

        buttonClearHistory = findViewById(R.id.button_clear_history)
        tvHistory = findViewById(R.id.tv_history)
        buttonClearHistory.setOnClickListener {
            TrackPreferences.removeAll()
            historyList.clear()
            showHistory()
        }
        App.sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, _ ->
            historyList.clear()
            historyList.addAll(TrackPreferences.read(sharedPreferences))
            historyAdapter.updateTracks(historyList)
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRequest()
                true
            }
            false
        }
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                showHistory()
            }
        }
    }

    private fun showHistory() {
        buttonClearHistory.isVisible = historyList.size > 0
        tvHistory.isVisible = historyList.size > 0
        recycler.adapter = historyAdapter
        historyAdapter.updateTracks(historyList)
    }

    private fun searchRequest() {
        placeholderLayout.isVisible = false
        if (inputEditText.text.isNotEmpty()) {
            imdbService.findTrack(inputEditText.text.toString()).enqueue(object :
                Callback<TrackResponse> {

                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    val queryStatus: QueryStatus = if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            recycler.adapter = trackAdapter
                            trackAdapter.updateTracks(tracks)
                            QueryStatus.SUCCESS
                        } else {
                            QueryStatus.NOT_FOUND
                        }
                    } else {
                        QueryStatus.NO_INTERNET
                    }
                    showMessage(queryStatus)
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) =
                    showMessage(QueryStatus.NO_INTERNET)

            })
        } else {
            recycler.adapter = historyAdapter
        }
    }

    private fun showMessage(queryStatus: QueryStatus) {
        if (queryStatus == QueryStatus.SUCCESS) {
            buttonClearHistory.isVisible = false
            tvHistory.isVisible = false
            return
        }
        if (queryStatus.message != -1) {
            placeholderLayout.isVisible = true
            buttonUpdate.isVisible = queryStatus.visibility
            tracks.clear()
            trackAdapter.updateTracks(tracks)
            placeholderMessage.text = getString(queryStatus.message)
            placeholderMessage.setCompoundDrawablesWithIntrinsicBounds(
                0,
                queryStatus.drawable,
                0,
                0
            )
        } else {
            placeholderLayout.isVisible = false
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(savedInstanceState.getString(INPUT_STRING))
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    companion object {
        const val INPUT_STRING = "input"
        const val imdbBaseUrl = "https://itunes.apple.com"
    }

    enum class QueryStatus(val message: Int, val drawable: Int, val visibility: Boolean) {
        NOT_FOUND(R.string.nothing_found, R.drawable.nothing, false),
        NO_INTERNET(R.string.something_went_wrong, R.drawable.internet, true),
        SUCCESS(-1, 0, false)
    }
}

