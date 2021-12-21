package com.ericliu.plugins

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        var weather = "sunny"
        get("/weatherforecast") {
            call.respondText(weather, contentType = ContentType.Text.Plain)
        }

        post("weatherforecast") {
            weather = call.receiveText()
            call.respondText("The weather has been set to: $weather", contentType = ContentType.Text.Plain)
        }

    }
}
