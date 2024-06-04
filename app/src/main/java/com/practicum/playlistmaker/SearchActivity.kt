package com.practicum.playlistmaker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
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

    private val imdbBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val imdbService = retrofit.create(IMDbApi::class.java)
    private val tracks = ArrayList<Track>()
    private var trackAdapter: TrackAdapter = TrackAdapter(tracks)
    private lateinit var inputEditText: EditText
    private lateinit var placeholderLayout: LinearLayout
    private lateinit var placeholderMessage: MaterialTextView
    private lateinit var buttonUpdate: MaterialButton
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
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    val input = s.toString()

                    savedInstanceState?.putString(Companion.INPUT_STRING, input)
                }
                clearButton.isVisible = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        val myToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.idSearchToolbar)
        setSupportActionBar(myToolbar)
        myToolbar.setNavigationOnClickListener { finish() }

        val recycler = findViewById<RecyclerView>(R.id.trackList)
        recycler.layoutManager = LinearLayoutManager(this)
//        recycler.adapter =if(isOnline(this))  {TrackAdapter(tracks = getTracks())  }else{null}

//        trackAdapter = TrackAdapter(tracks = tracks)

        recycler.adapter = trackAdapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                if (inputEditText.text.isNotEmpty()) {
                    imdbService.findTrack(inputEditText.text.toString()).enqueue(object :
                        Callback<TrackResponse> {

                        override fun onResponse(
                            call: Call<TrackResponse>,
                            response: Response<TrackResponse>
                        ) {
                            if (response.code() == 200) {
                                tracks.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    tracks.addAll(response.body()?.results!!)
                                    trackAdapter.notifyDataSetChanged()
                                }
                                if (tracks.isEmpty()) {
//                                    val internet: Int = R.drawable.internet
                                    showMessage(
                                        getString(R.string.nothing_found),
                                        "",
                                        R.drawable.nothing,
                                        false
                                    )
                                } else {
                                    showMessage("", "", 0, false)
                                }
                            } else {
                                showMessage(
                                    getString(R.string.something_went_wrong),
                                    response.code().toString(),
                                    R.drawable.internet,
                                    true
                                )
                            }
                        }

                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            showMessage(
                                getString(R.string.something_went_wrong),
                                t.message.toString(),
                                R.drawable.internet,
                                true
                            )

                        }

                    })
                }
                true
            }
            false
        }
    }

    private fun showMessage(
        text: String,
        additionalMessage: String,
        drawable: Int,
        visibility: Boolean
    ) {
        if (text.isNotEmpty()) {
            placeholderLayout.isVisible = true
            buttonUpdate.isVisible = visibility
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            placeholderMessage.text = text

            placeholderMessage.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)
//            if (additionalMessage.isNotEmpty()) {
//                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
//                    .show()
//            }
        } else {
            placeholderMessage.isVisible = false
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun getTracks() = listOf<Track>(

        Track(
            "Smells Like Teen Spirit",
            "Nirvana",
            "5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ),

        Track(
            "Billie Jean",
            "Michael Jackson",
            "4:35",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ),

        Track(
            "Stayin' Alive",
            "Bee Gees",
            "4:10",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ),
        Track(
            "Whole Lotta Love",
            "Led Zeppelin", "5:33",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ),
        Track(
            "Sweet Child O'Mine",
            "Guns N' Roses",
            "5:03",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        )
    )

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(savedInstanceState.getString(Companion.INPUT_STRING))
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    companion object {
        const val INPUT_STRING = "input"
    }

}

