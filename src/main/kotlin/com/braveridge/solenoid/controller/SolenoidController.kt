package com.braveridge.solenoid.controller

import com.braveridge.solenoid.form.WebhookForm
import com.braveridge.solenoid.properties.BravegateProperties
import com.braveridge.solenoid.service.SolenoidService
import mu.KLogger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/solenoid")
class SolenoidController(private val solenoidService: SolenoidService,
                         private val bravegateProperties: BravegateProperties,
                         private val logger: KLogger) {

    @GetMapping("")
    fun index(model: Model): String {
        model.addAttribute("deviceId", bravegateProperties.deviceId)
        return "solenoid/index"
    }

    @PostMapping("webhook")
    @ResponseBody
    fun webhook(@RequestBody @Validated form: WebhookForm): String {
        logger.info { "webhook data from BraveGATE: $form" }
        solenoidService.notifyIfNeed(form)
        return "OK"
    }
}