package io.github.vinccool96.observable.internal.exception

expect object ExceptionHandler {

    fun handleUncaughtException(e: Exception)

}