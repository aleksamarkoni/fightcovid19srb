package com.volontero.util.resource_proivder

interface ResourceProvider {
    fun getEndangeredPerson(): String
    fun getVolunteer(): String
    fun getMoreDetails(): String
}