package com.ericliu

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {

        }
    }

    install(Routing) {
        get("/goodevening") {
            call.respondText("Good evening, world!")
        }
    }

    routing {
        trace { application.log.trace(it.buildText()) }
        route("/weather") {
            get("/usa") {
                call.respondText { "The weather in US: Snow" }
            }


            route("/europe", HttpMethod.Get) {
                header("systemtoken", "weathersystem") {
                    param("name") {
                        handle {
                            val name = call.parameters.get("name")
                            call.respondText { "Haha $name, The weather in Europe is: raining" }
                        }
                    }

                    handle {
                        call.respondText { "The weather in europe: sunny" }
                    }
                }
            }
        }


        get("/") {
            call.respondText("Hello, World!", contentType = ContentType.Text.Plain)
        }

        get("/json/json") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
