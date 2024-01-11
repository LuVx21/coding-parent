package org.luvx.coding.common.enhancer

import mu.KotlinLogging
import java.util.Arrays
import java.util.function.Function
import java.util.stream.Collectors

private val log = KotlinLogging.logger {}

fun Any?.out(vararg messages: Any?): Any? {
    println(this)
    if (messages.isNotEmpty()) {
        println("-----------------")
        println(messages.contentDeepToString())
    }
    return this
}


fun Any?.nonNull(a: Function<Any?, Any?>): Any? {
    if (this != null) {
        return a.apply { this }
    }
    return null
}

fun out(vararg messages: Any?) {
    if (messages.isNullOrEmpty()) {
        return
    }
    Arrays.stream(messages)
            .map { s -> s.toString() }
            .collect(Collectors.joining(""))
            .out()
    // println(messages.contentDeepToString())
}