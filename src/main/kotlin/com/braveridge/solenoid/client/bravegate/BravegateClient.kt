package com.braveridge.solenoid.client.bravegate

import com.braveridge.solenoid.client.bravegate.request.AuthRequest
import com.braveridge.solenoid.client.bravegate.request.CommandRequest
import com.braveridge.solenoid.client.bravegate.response.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BravegateClient {
    @POST("v1/auth")
    fun auth(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("v1/commands")
    fun postCommand(@Header("X-Braveridge-API-Key") apiKey: String,
                    @Header("X-Braveridge-Token") token: String,
                    @Body commandRequest: CommandRequest
    ): Call<Void>
}