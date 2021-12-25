package com.ericliu

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
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

    install(StatusPages){
        statusFile(
            HttpStatusCode.InternalServerError,
            HttpStatusCode.NotFound,
            filePattern = "customerrors/myerror#.html"
        )
        exception<FirstException> { firstException ->
            call.respond(HttpStatusCode.NotFound)
            log.error(firstException.localizedMessage)
            throw firstException
        }
        exception<SecondException> { secondException ->
            call.respondRedirect("/", false)
            throw secondException
        }
    }

    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = GsonSerializer()
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

        get("/spaceship") {
            call.respond(Spaceship("Serenity"))
        }

        get("/consumeservice") {
            val result = client.get<Spaceship>("http://localhost:8080/spaceship")
            log.info("the result: $result")
            call.respond(result)
        }

        get("/firstexception"){
            throw FirstException()
        }

        get("/secondexception") {
            throw SecondException()
        }
    }
}

data class Spaceship(val name: String)

class FirstException: RuntimeException()
class SecondException: RuntimeException()
