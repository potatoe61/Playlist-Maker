package com.example.playlistmaker.setting.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.setting.domain.api.SharingInteractor
import com.example.playlistmaker.setting.domain.api.SwitchThemeInteractor

class SettingViewModel(private val sharingInteractor: SharingInteractor,
    private val switchThemeInteractor: SwitchThemeInteractor
): ViewModel() {

    private val themeAppLiveData = MutableLiveData<Boolean>()

    init {
        themeAppLiveData.value = switchThemeInteractor.getSharedPreferencesThemeValue()
    }
    fun getTheme():Boolean{
        return switchThemeInteractor.getSharedPreferencesThemeValue()
    }
    fun editTheme(checked:Boolean){
        switchThemeInteractor.sharedPreferencesEdit(checked)
    }
    fun switchTheme(checked: Boolean){
        switchThemeInteractor.switchTheme(checked)
        editTheme(checked)
        themeAppLiveData.value = checked
    }
    fun supportSend(){
        sharingInteractor.openSupport()
    }
    fun openTerm(){
        sharingInteractor.openTerms()
    }
    fun shareApp(){
        sharingInteractor.shareApp()
    }
}
