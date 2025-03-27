package com.example.playlistmaker.setting.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.viewModels
import com.example.playlistmaker.R
import com.example.playlistmaker.setting.presentation.viewModel.SettingViewModel
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private val viewModelSetting by viewModels<SettingViewModel> { SettingViewModel.getViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<Button>(R.id.back)
        val switchTheme = findViewById<SwitchMaterial>(R.id.themeSwitch)

        viewModelSetting.getThemeAppLiveData().observe(this) { isDarkThemeEnabled ->
            switchTheme.isChecked = isDarkThemeEnabled
        }
        switchTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModelSetting.switchTheme(checked)
        }

        backButton.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        val supportButton = findViewById<FrameLayout>(R.id.support)
        supportButton.setOnClickListener{
            viewModelSetting.supportSend()
        }
        val agreementButton = findViewById<FrameLayout>(R.id.agreement)
        agreementButton.setOnClickListener{
            viewModelSetting.openTerm()
        }
        val shareButton = findViewById<FrameLayout>(R.id.share)
        shareButton.setOnClickListener{
            viewModelSetting.shareApp()
        }
    }
}