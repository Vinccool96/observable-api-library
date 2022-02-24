package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.value.ObservableObjectValueStub
import io.github.vinccool96.uncaught.Handler
import io.github.vinccool96.uncaught.UncaughtExceptionHandler
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JVMExpressionHelperTest {

    private var helper: ExpressionHelper<Any>? = null

    private lateinit var observable: ObservableObjectValueStub<Any>

    @BeforeTest
    fun setUp() {
        this.helper = null
        this.observable = ObservableObjectValueStub(DATA_1)
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInSingleInvalidation() {
        val called = AtomicBoolean()

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.set(true) }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }

        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertTrue(called.get())
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInMultipleInvalidation() {
        val called = AtomicInteger()

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.incrementAndGet() }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }

        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertEquals(2, called.get())
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInSingleChange() {
        val called = AtomicBoolean()

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.set(true) }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }

        this.observable.set(DATA_2)
        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertTrue(called.get())
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInMultipleChange() {
        val called = AtomicInteger()

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.incrementAndGet() }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }

        this.observable.set(DATA_2)
        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertEquals(2, called.get())
    }

    @Test
    fun testExceptionHandledByThreadUncaughtHandlerInMultipleChangeAndInvalidation() {
        val called = AtomicInteger()

        UncaughtExceptionHandler.threadProperties.handler = Handler { called.incrementAndGet() }

        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _, _, _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }
        this.helper = ExpressionHelper.addListener(this.helper, this.observable) { _ -> throw RuntimeException() }

        this.observable.set(DATA_2)
        ExpressionHelper.fireValueChangedEvent(this.helper)
        assertEquals(4, called.get())
    }

    companion object {

        private val DATA_1: Any = Any()

        private val DATA_2: Any = Any()

    }

}