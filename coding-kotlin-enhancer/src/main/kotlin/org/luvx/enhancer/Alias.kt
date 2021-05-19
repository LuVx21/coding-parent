package org.luvx.enhancer

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

fun Any?.out(vararg messages: Any?): Any? {
    println(this)
    if (messages.isNotEmpty()) {
        println("-----------------")
        println(messages.contentDeepToString())
    }
    return this
}

fun out(vararg messages: Any?) {
    println(messages.contentDeepToString())
}