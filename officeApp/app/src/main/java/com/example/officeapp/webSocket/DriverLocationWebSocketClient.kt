package com.example.officeapp.webSocket

import com.example.officeapp.models.location.DriverLocation
import com.example.officeapp.utils.BaseWebSocketUrl
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriverLocationWebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    @BaseWebSocketUrl private val baseWebSocketUrl: String
) {
    private var webSocket: WebSocket? = null

    fun connect(
        onLocationReceived: (DriverLocation) -> Unit,
        onConnected: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        if (webSocket != null) {
            return
        }

        val request = Request.Builder()
            .url("$baseWebSocketUrl/ws-locations")
            .build()

        webSocket = okHttpClient.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onOpen(
                    webSocket: WebSocket,
                    response: okhttp3.Response
                ) {
                    val connectFrame = buildString {
                        append("CONNECT\n")
                        append("accept-version:1.2\n")
                        append("host:localhost\n")
                        append("\n")
                        append('\u0000')
                    }

                    webSocket.send(connectFrame)
                }

                override fun onMessage(
                    webSocket: WebSocket,
                    text: String
                ) {
                    when {
                        text.startsWith("CONNECTED") -> {
                            val subscribeFrame = buildString {
                                append("SUBSCRIBE\n")
                                append("id:driver-locations-subscription\n")
                                append("destination:/topic/driver-locations\n")
                                append("ack:auto\n")
                                append("\n")
                                append('\u0000')
                            }

                            webSocket.send(subscribeFrame)
                            onConnected()
                        }

                        text.startsWith("MESSAGE") -> {
                            val body = extractStompBody(text)
                            if (body.isNotBlank()) {
                                try {
                                    val driverLocation = gson.fromJson(
                                        body,
                                        DriverLocation::class.java
                                    )

                                    onLocationReceived(driverLocation)
                                } catch (ex: Exception) {
                                    onError(ex.message
                                        ?: "Failed to parse WebSocket location message.")
                                }
                            }
                        }

                        text.startsWith("ERROR") -> {
                            onError(text)
                        }
                    }
                }

                override fun onFailure(
                    webSocket: WebSocket,
                    t: Throwable,
                    response: okhttp3.Response?
                ) {
                    this@DriverLocationWebSocketClient.webSocket = null
                    onError(t.message ?: "WebSocket connection failed.")
                }

                override fun onClosed(
                    webSocket: WebSocket,
                    code: Int,
                    reason: String
                ) {
                    this@DriverLocationWebSocketClient.webSocket = null
                }
            }
        )
    }

    fun disconnect() {
        val disconnectFrame = buildString {
            append("DISCONNECT\n")
            append("\n")
            append('\u0000')
        }

        webSocket?.send(disconnectFrame)
        webSocket?.close(1000, "Screen closed")
        webSocket = null
    }

    private fun extractStompBody(message: String): String {
        val bodyStartIndex = message.indexOf("\n\n")

        if (bodyStartIndex == -1) {
            return ""
        }

        return message
            .substring(bodyStartIndex + 2)
            .replace("\u0000", "")
            .trim()
    }
}