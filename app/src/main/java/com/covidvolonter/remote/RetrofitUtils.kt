package com.covidvolonter.remote

import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RetrofitUtils @Inject constructor(val gson: Gson) {
    fun <T> handleResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody == null)
                throw EmptyResponse()
            else
                return responseBody
        } else {
            //TODO if we have errors work with Marko to standardize the error structure
//            val errorResponse =
//                    gson.fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
//            if (errorResponse.serverErrorCode == ServerErrorCodes.BAD_REQUEST) {
//                throw AuthenticationRequired(errorResponse.reason)
//            }
            throw IOException()
        }
    }
}