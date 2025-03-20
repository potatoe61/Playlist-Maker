package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.back)
        val switchTheme = findViewById<SwitchMaterial>(R.id.themeSwitch)
        val shareButton = findViewById<FrameLayout>(R.id.share)
        shareButton.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_practicum_course_link))
            shareIntent.type="text/plain"
            startActivity(shareIntent)
        }
        val switchThemeInteractor = Creator.provideSwitchThemeInteractor()
        switchTheme.isChecked = switchThemeInteractor.getSharedPreferencesThemeValue()
        switchTheme.setOnCheckedChangeListener { switcher, checked ->
            switchThemeInteractor.sharedPreferencesEdit(checked)
            switchThemeInteractor.switchTheme(checked)

        }

        backButton.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        val supportImage = findViewById<FrameLayout>(R.id.support)
        supportImage.setOnClickListener{
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf( getString(R.string.support_email)))
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            mailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
            startActivity(mailIntent)
        }
        val linkImage = findViewById<FrameLayout>(R.id.agreement)
        linkImage.setOnClickListener {
            val url = getString(R.string.practicum_offer_link)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }
}