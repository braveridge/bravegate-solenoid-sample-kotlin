package com.braveridge.solenoid.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("bravegate")
class BravegateProperties {
    lateinit var apiUrl: String
    lateinit var authkeyId: String
    lateinit var authkeySecret: String
    lateinit var deviceId: String
}