package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.logging.Log
import io.github.vinccool96.logging.LogHandler
import io.github.vinccool96.logging.LogLevel
import kotlin.native.concurrent.ThreadLocal
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ErrorLoggingUtility {

    private var level: LogLevel? = null

    private var lastLog: Log? = null

    private val handler = LogHandler { log: Log ->
        this@ErrorLoggingUtility.lastLog = log
        this@ErrorLoggingUtility.level = log.level
    }

    fun start() {
        reset()
        logger.baseHandlerActivated = false
        logger.addHandler(this.handler)
    }

    fun stop() {
        logger.removeHandler(this.handler)
        logger.baseHandlerActivated = true
    }

    fun reset() {
        this.lastLog = null
        this.level = null
    }

    fun checkFine(expectedException: KClass<out Throwable>) {
        check(LogLevel.INFO, expectedException)
    }

    fun check(expectedLevel: LogLevel, expectedException: KClass<out Throwable>) {
        assertNotNull(this.lastLog)
        assertEquals(expectedLevel, this.lastLog!!.level)
        assertEquals(expectedException, this.lastLog!!.e!!::class)
        reset()
    }

    @ThreadLocal
    companion object {

        private val logger = Logging.logger

    }

}