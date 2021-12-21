package com.ericliu.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random

fun main(args: Array<String>) = runBlocking {
    withContext(Dispatchers.IO) {
        launch {
            firstCoroutine(1)
        }
        println("done with the context.")
    }
}

suspend fun firstCoroutine(id: Int) {
    delay(Random.nextLong() * 2000)
    println("first $id")
}