package com.example.playlistmaker.setting.data.repositories

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.setting.domain.repositories.ExternalNavigatorRepository

import com.example.playlistmaker.R
import com.example.playlistmaker.setting.domain.model.EmailData

class ExternalNavigatorRepositoryImpl(val context: Context): ExternalNavigatorRepository {


    override fun openLink(link: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(link)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openEmail(email: EmailData) {
        val mailIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email)))
            putExtra(Intent.EXTRA_SUBJECT,context.getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT,context.getString(R.string.email_message))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(mailIntent)
    }

    override fun shareLink(text: String) {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,text)
            type = "text/plain"
            context.startActivity(
                Intent.createChooser(this,null).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

}