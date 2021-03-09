package com.braveridge.solenoid.client.bravegate.request

data class CommandRequest(
        val name: String,
        val params: Map<String, Any>,
        val targets: Target) {

    data class Target(val devices: List<String>,
                      val routers: List<String>,
                      val groups: List<String>)

}
