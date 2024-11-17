package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.data.SharedPreferenceRepositoryImp
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.TracksRepositoryImpl
import com.practicum.playlistmaker.data.preferences.SettingsManager
import com.practicum.playlistmaker.data.preferences.TrackManager
import com.practicum.playlistmaker.domain.api.SettingsInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferenceRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(), getTrackManager(context))
    }

    private fun getTrackManager(context: Context): TrackManager {
        return TrackManager(context)
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun getSettingInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSharedPreferenceRepository(context))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSharedPreferenceRepository(context))
    }

    private fun getSharedPreferenceRepository(context: Context): SharedPreferenceRepository {
        return SharedPreferenceRepositoryImp(getSettingsManager(context))
    }

    private fun getSettingsManager(context: Context): SettingsManager {
        return SettingsManager(context)
    }
}