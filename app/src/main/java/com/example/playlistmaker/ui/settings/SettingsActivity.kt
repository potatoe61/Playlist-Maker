package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.app.App
import com.example.playlistmaker.presentation.app.SHARED_PREFERENCES
import com.example.playlistmaker.presentation.app.THEME
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var themeSwitcher: SwitchMaterial
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        themeSwitcher = findViewById(R.id.themeSwitch)
        themeSwitcher.isChecked = sharedPreferences.getBoolean(THEME, false)
        val shareLink = findViewById<FrameLayout>(R.id.share)
        shareLink.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_practicum_course_link))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val supportImage = findViewById<FrameLayout>(R.id.support)
        supportImage.setOnClickListener {
            val recipient = getString(R.string.support_email)
            val subject = getString(R.string.email_subject)
            val message = getString(R.string.email_message)
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data =
                    Uri.parse("mailto:$recipient") // Только email-приложения будут обрабатывать это
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(emailIntent)
        }
        val linkImage = findViewById<FrameLayout>(R.id.agreement)
        linkImage.setOnClickListener {
            val url = getString(R.string.practicum_offer_link)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
        val backImage = findViewById<Button>(R.id.back)
        backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)

            sharedPreferences.edit()
                .putBoolean(THEME, checked)
                .apply()
        }
    }
}
