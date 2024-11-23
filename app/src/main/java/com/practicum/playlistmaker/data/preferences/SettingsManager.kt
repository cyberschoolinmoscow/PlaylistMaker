package com.practicum.playlistmaker.data.preferences

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R

class SettingsManager(private val context: Context) {
    val sharedPreferences = context.getSharedPreferences("MODE", Context.MODE_PRIVATE)

    fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody = context.getString(R.string.share_btn_link)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context.startActivity(Intent.createChooser(intent, "Share"))
    }

    fun help() {
        val message = context.getString(R.string.help_btn_message)
        val subject = context.getString(R.string.help_btn_subject)
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email)))
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(shareIntent)
    }


}