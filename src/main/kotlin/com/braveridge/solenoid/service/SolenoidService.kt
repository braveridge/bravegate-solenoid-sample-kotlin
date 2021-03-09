package com.braveridge.solenoid.service

import com.braveridge.solenoid.client.bravegate.request.CommandRequest
import com.braveridge.solenoid.form.WebhookForm
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KLogger
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.io.IOException
import javax.validation.constraints.NotBlank

@Service
class SolenoidService(private val bravegateService: BravegateService,
                      private val mapper: ObjectMapper,
                      private val coroutineScope: CoroutineScope,
                      private val logger: KLogger) {
    companion object {
        const val sensorId = "00f0"
    }

    private var sessionMap = mutableMapOf<WebSocketSession, String>()

    fun handleMessage(session: WebSocketSession, message: TextMessage) {
        val request: Request = mapper.readValue(message.payload, Request::class.java)
        prepareToRequestCommand(request, session)
        val response = sendCommandToBraveGate(request)
            ?.let { commandId ->
                logger.info { "send command to BraveGATE: $commandId" }

                when (request.command) {
                    RequestCommand.On, RequestCommand.Off -> {
                        coroutineScope.launch {
                            delay(1000)
                            sendStateCommandToBraveGate(request)
                                ?.also { logger.info { "send command to BraveGATE: $it" } }
                        }
                    }
                    else -> {}
                }
                Response(ResponseType.Command, ResponseState.Success, request.device)
            }
            ?: Response(ResponseType.Command, ResponseState.Failed, request.device)

        val outputMessage = TextMessage(mapper.writeValueAsString(response))
        session.sendMessage(outputMessage)
    }

    fun notifyIfNeed(webhookForm: WebhookForm) {
        sessionMap
            .filterKeys { !it.isOpen }
            .forEach { (session, _) ->
                sessionMap.remove(session)
            }

        sessionMap
            .filterValues { it == webhookForm.device.deviceId }
            .forEach { (session, deviceId ) ->
                val state = if (webhookForm.device.data["data"] == RequestCommand.On.encodedData) ResponseState.On else ResponseState.Off
                val response = Response(ResponseType.Notify, state, deviceId)
                sendMessage(session, response)
            }
    }

    private fun sendMessage(session: WebSocketSession, response: Response) {
        try {
            val outputMessage = TextMessage(mapper.writeValueAsString(response))
            session.sendMessage(outputMessage)
        } catch (e: IOException) {
            logger.warn { e.message }
        }
    }

    private fun sendCommandToBraveGate(request: Request): String? {
        val commandParams = mutableMapOf("sensor_id" to sensorId)
        request.command.encodedData?.let {
            commandParams["data"] = it
        }
        val commandRequest = CommandRequest(request.command.commandName, commandParams, CommandRequest.Target(listOf(request.device), listOf(), listOf()))
        return bravegateService.postCommand(commandRequest)
    }

    private fun sendStateCommandToBraveGate(request: Request): String? {
        val stateRequest = Request().apply {
            command = RequestCommand.State
            device = request.device
        }
        return sendCommandToBraveGate(stateRequest)
    }

    private fun prepareToRequestCommand(request: Request, session: WebSocketSession) {
        sessionMap[session] = request.device
    }

    class Request {
        @NotBlank
        lateinit var command: RequestCommand
        @NotBlank
        lateinit var device: String
    }
    enum class RequestCommand(val commandName: String, val encodedData: String? = null) {
        State("SEND_DATA_AT_ONCE"),
        On("GENERAL_COMMAND01", "AA=="),
        Off("GENERAL_COMMAND01", "AQ==");
    }

    data class Response(
        val type: ResponseType,
        val state: ResponseState,
        val device: String
    )

    enum class ResponseType {
        Command, Notify
    }
    enum class ResponseState {
        Success, Failed, On, Off
    }
}