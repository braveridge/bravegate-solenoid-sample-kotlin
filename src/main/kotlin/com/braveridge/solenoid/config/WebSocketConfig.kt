package com.braveridge.solenoid.config

import com.braveridge.solenoid.websocket.SolenoidWebSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class WebSocketConfig(private val solenoidWebSocketHandler: SolenoidWebSocketHandler): WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(solenoidWebSocketHandler, "/ws/solenoid")
    }

    @Bean
    fun taskScheduler(): TaskScheduler = ThreadPoolTaskScheduler()
        .apply {
            poolSize = 3
        }
}