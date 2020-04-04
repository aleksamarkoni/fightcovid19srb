package com.covidvolonter.util.resource_proivder

import android.app.Application
import android.content.Context
import com.google.fightcorona.R

class AndroidResourceProvider(context: Application) : ResourceProvider {

    private val context: Context

    init {
        this.context = context
    }

    override fun getEndangeredPerson(): String {
        return context.getString(R.string.endangered_person)
    }

    override fun getVolunteer(): String {
        return context.getString(R.string.volunteer)
    }

    override fun getMoreDetails(): String {
        return context.getString(R.string.more_details)
    }
}