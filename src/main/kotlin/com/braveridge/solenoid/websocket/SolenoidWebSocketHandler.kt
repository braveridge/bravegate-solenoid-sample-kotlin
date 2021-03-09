package com.braveridge.solenoid.websocket

import com.braveridge.solenoid.service.SolenoidService
import mu.KLogger
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class SolenoidWebSocketHandler(private val solenoidService: SolenoidService,
                               private val logger: KLogger): TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info { "connected from ${session.remoteAddress}" }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info { "message: ${message.payload}" }
        solenoidService.handleMessage(session, message)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info { "finished connection" }
    }
}