package io.github.vinccool96.observable.internal.exception

actual object ExceptionHandler {

    actual fun handleUncaughtException(e: Exception) {
        println(e)
    }

}