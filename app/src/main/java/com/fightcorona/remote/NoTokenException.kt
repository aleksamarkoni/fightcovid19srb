package com.fightcorona.remote

import java.io.IOException

class NoTokenException(message: String = "User not logged in") : IOException(message)