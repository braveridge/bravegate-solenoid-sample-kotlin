package com.braveridge.solenoid

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SolenoidApplication

fun main(args: Array<String>) {
    runApplication<SolenoidApplication>(*args)
}
