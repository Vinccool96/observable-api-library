package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.value.ObservableObjectValueStub
import io.github.vinccool96.uncaught.Handler
import io.github.vinccool96.uncaught.UncaughtExceptionHandler
import kotlin.native.concurrent.AtomicInt
import kotlin.native.concurrent.AtomicReference
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NativeExpressionHelperTest {

    private var helper: ExpressionHelper<Any>? = null

    private lateinit var observable: ObservableObjectValueStub<Any>

    @BeforeTest
    fun setUp() {
        this.helper = null
        this.observable = ObservableObjectValueStub(DATA_1)
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInSingleInvalidation() {
        val called = AtomicReference(false)

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.value = true }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }

        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertTrue(called.value)
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInMultipleInvalidation() {
        val called = AtomicInt(0)

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.increment() }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }

        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertEquals(2, called.value)
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInSingleChange() {
        val called = AtomicReference(false)

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.value = true }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }

        this.observable.set(DATA_2)
        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertTrue(called.value)
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInMultipleChange() {
        val called = AtomicInt(0)

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.increment() }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }

        this.observable.set(DATA_2)
        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertEquals(2, called.value)
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInMultipleChangeAndInvalidation() {
        val called = AtomicInt(0)

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.increment() }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }

        this.observable.set(DATA_2)
        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertEquals(4, called.value)
    }

    companion object {

        private val DATA_1: Any = Any()

        private val DATA_2: Any = Any()

    }

}