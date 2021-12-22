package com.ericliu.coroutines

import com.ericliu.extension.addFive
import kotlinx.coroutines.*
import kotlin.random.Random

fun main(args: Array<String>) = runBlocking {
    withContext(Dispatchers.IO) {
        repeat(1000) {
            launch {
                firstCoroutine(it.addFive())
            }
        }
        println("done with the context.")
    }
}

suspend fun firstCoroutine(id: Int) {
    delay(Random.nextLong() * 2000)
    println("first $id")
}