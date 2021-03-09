package com.braveridge.solenoid.client

import mu.KLogger
import org.springframework.stereotype.Component
import retrofit2.Call

@Component
class RetrofitExecutor(private val logger: KLogger) {
    fun <T> execute(call: Call<T>): RetrofitResult<T> {
        val response = call.execute()
        if (response.isSuccessful) {
            return RetrofitResult(false, response.headers().toMultimap(), response.body(), null)
        }

        val errorBody = response.errorBody()?.string()
        logger.error { "error occurred on requesting, ${errorBody ?: ""}" }
        return RetrofitResult(true, response.headers().toMultimap(), null, errorBody)
    }
}