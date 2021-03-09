package com.braveridge.solenoid.client.bravegate

import java.io.Serializable

data class AuthToken(val apiKey: String, val token: String) : Serializable
