package com.example.playlistmaker.setting.data.repositories

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.setting.domain.model.EmailData
import com.example.playlistmaker.setting.domain.repositories.SharingRepository

class SharingRepositoryImpl(val context : Context) :SharingRepository {

    override fun getShareAppLink(): String {
        return context.getString(R.string.agreement)
    }

    override fun getSupportEmailData():EmailData {
        return EmailData(
            context.getString(R.string.support_email),
            context.getString(R.string.email_subject),
            context.getString(R.string.email_message))
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.practicum_offer_link)
    }
}