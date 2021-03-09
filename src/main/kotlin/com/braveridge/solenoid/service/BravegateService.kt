package com.braveridge.solenoid.service

import com.braveridge.solenoid.client.RetrofitExecutor
import com.braveridge.solenoid.client.bravegate.AuthTokenResolver
import com.braveridge.solenoid.client.bravegate.BravegateClient
import com.braveridge.solenoid.client.bravegate.exception.BravegateException
import com.braveridge.solenoid.client.bravegate.request.CommandRequest
import com.braveridge.solenoid.properties.BravegateProperties
import org.springframework.stereotype.Service


@Service
class BravegateService(private val client: BravegateClient,
                       private val executor: RetrofitExecutor,
                       private val authTokenResolver: AuthTokenResolver,
                       private val bravegateProperties: BravegateProperties) {

    fun postCommand(request: CommandRequest): String? {
        val authToken = authTokenResolver.resolve(bravegateProperties.authkeyId, bravegateProperties.authkeySecret)
            ?: return null

        val call = client.postCommand(authToken.apiKey, authToken.token, request)
        val result = executor.execute(call)
        if (result.hasError) {
            result.errorBody
                ?.let { throw BravegateException(it) }
                ?: return null
        }

        return result.headers["location"]?.firstOrNull()?.split("/")?.lastOrNull()
    }
}