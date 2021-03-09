package com.braveridge.solenoid.config

import com.braveridge.solenoid.client.bravegate.BravegateClient
import com.braveridge.solenoid.coroutine.SpringCoroutineScope
import com.braveridge.solenoid.properties.BravegateProperties
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import mu.KLogger
import mu.KotlinLogging
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


@Configuration
class AppConfig {
    @Bean
    fun logger(): KLogger = KotlinLogging.logger {}

    @Bean
    fun coroutineScope(): CoroutineScope = SpringCoroutineScope

    @Bean
    fun jacksonConverterFactory(mapper: ObjectMapper): JacksonConverterFactory =
        JacksonConverterFactory.create(mapper)

    @Bean
    fun bravegateRetrofit(jacksonConverterFactory: JacksonConverterFactory, bravegateProperties: BravegateProperties): Retrofit
        = Retrofit.Builder()
            .baseUrl(bravegateProperties.apiUrl)
            .addConverterFactory(jacksonConverterFactory)
            .client(OkHttpClient.Builder().build())
            .build()

    @Bean
    fun bravegateClient(bravegateRetrofit: Retrofit) = bravegateRetrofit.create(BravegateClient::class.java)
}