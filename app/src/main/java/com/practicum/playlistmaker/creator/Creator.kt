package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.App
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
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(), getTrackManager())
    }

    private fun getTrackManager(): TrackManager {
        return TrackManager()
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getSettingInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSharedPreferenceRepository())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSharedPreferenceRepository())
    }

    private fun getSharedPreferenceRepository(): SharedPreferenceRepository {
        return SharedPreferenceRepositoryImp()
    }

    private fun getSettingsManager(): SettingsManager {
        return SettingsManager(App.getContext())
    }
}