package io.github.vinccool96.observable.internal.exception

actual object ExceptionHandler {

    actual fun handleUncaughtException(e: Exception) {
        Thread.currentThread().uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e)
    }

}