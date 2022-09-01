package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.value.ChangeListener
import io.github.vinccool96.observable.beans.value.ObservableBooleanValue
import kotlin.native.internal.GC
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.fail

@Suppress("UNUSED_VALUE", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class NativeBindingsBooleanTest {

    @Test
    fun testAnd_WeakReference() {
        val op1 = ObservableBooleanValueMock()
        val op2 = ObservableBooleanValueMock()
        andBinding(op1, op2)
        GC.collect()
        op1.fireInvalidationEvent()
        assertNull(op1.listener)
        assertNotNull(op2.listener)
        op2.fireInvalidationEvent()
        assertNull(op1.listener)
        assertNull(op2.listener)
    }

    private fun andBinding(op1: ObservableBooleanValueMock, op2: ObservableBooleanValueMock) {
        var binding: BooleanBinding? = Bindings.and(op1, op2)
        assertNotNull(op1.listener)
        assertNotNull(op2.listener)
        binding = null
    }

    @Test
    fun testOr_WeakReference() {
        val op1 = ObservableBooleanValueMock()
        val op2 = ObservableBooleanValueMock()
        orBinding(op1, op2)
        GC.collect()
        op1.fireInvalidationEvent()
        assertNull(op1.listener)
        assertNotNull(op2.listener)
        op2.fireInvalidationEvent()
        assertNull(op1.listener)
        assertNull(op2.listener)
    }

    private fun orBinding(op1: ObservableBooleanValueMock, op2: ObservableBooleanValueMock) {
        var binding: BooleanBinding? = Bindings.or(op1, op2)
        assertNotNull(op1.listener)
        assertNotNull(op2.listener)
        binding = null
    }

    @Test
    fun testEqual_WeakReference() {
        val op1 = ObservableBooleanValueMock()
        val op2 = ObservableBooleanValueMock()
        equalBinding(op1, op2)
        GC.collect()
        op1.fireInvalidationEvent()
        assertNull(op1.listener)
        assertNotNull(op2.listener)
        op2.fireInvalidationEvent()
        assertNull(op1.listener)
        assertNull(op2.listener)
    }

    private fun equalBinding(op1: ObservableBooleanValueMock, op2: ObservableBooleanValueMock) {
        var binding: BooleanBinding? = Bindings.or(op1, op2)
        assertNotNull(op1.listener)
        assertNotNull(op2.listener)
        binding = null
    }

    @Test
    fun testNotEqual_WeakReference() {
        val op1 = ObservableBooleanValueMock()
        val op2 = ObservableBooleanValueMock()
        notEqualBinding(op1, op2)
        GC.collect()
        op1.fireInvalidationEvent()
        assertNull(op1.listener)
        assertNotNull(op2.listener)
        op2.fireInvalidationEvent()
        assertNull(op1.listener)
        assertNull(op2.listener)
    }

    private fun notEqualBinding(op1: ObservableBooleanValueMock, op2: ObservableBooleanValueMock) {
        var binding: BooleanBinding? = Bindings.or(op1, op2)
        assertNotNull(op1.listener)
        assertNotNull(op2.listener)
        binding = null
    }

    private class ObservableBooleanValueMock : ObservableBooleanValue {

        var listener: InvalidationListener? = null

        override fun get(): Boolean {
            return false
        }

        override val value: Boolean
            get() = false

        fun fireInvalidationEvent() {
            val curListener = this.listener
            if (curListener != null) {
                curListener.invalidated(this)
            } else {
                fail("Attempt to fire an event with no listener attached")
            }
        }

        override fun addListener(listener: InvalidationListener) {
            if (hasListener(listener)) {
                fail("More than one listener set in mock.")
            }
            this.listener = listener
        }

        override fun removeListener(listener: InvalidationListener) {
            if (!hasListener(listener)) {
                fail("Attempt to remove unknown listener")
            }
            this.listener = null
        }

        override fun hasListener(listener: InvalidationListener): Boolean {
            return this.listener == listener
        }

        override fun addListener(listener: ChangeListener<in Boolean?>) {
            // not used
        }

        override fun removeListener(listener: ChangeListener<in Boolean?>) {
            // not used
        }

        override fun hasListener(listener: ChangeListener<in Boolean?>): Boolean {
            // not used
            return false
        }

    }

}