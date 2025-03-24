package com.example.playlistmaker.setting.domain.interactors

import com.example.playlistmaker.setting.domain.repositories.SwitchThemeRepository
import com.example.playlistmaker.setting.domain.api.SwitchThemeInteractor

class SwitchThemeInteractorImpl(private val repository : SwitchThemeRepository):
    SwitchThemeInteractor {


    override fun switchTheme(darkThemeEnabled: Boolean) {
        return repository.switchTheme(darkThemeEnabled)
    }

    override fun sharedPreferencesEdit(checked: Boolean) {
        return repository.sharedPreferencesEdit(checked)
    }

    override fun getSharedPreferencesThemeValue():Boolean {
        return repository.getSharedPreferencesThemeValue()
    }

}