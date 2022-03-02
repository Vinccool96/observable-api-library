@file:Suppress("unused")

package io.github.vinccool96.observable.beans.binding

import kotlin.test.Test
import kotlin.test.assertTrue

abstract class NumberBindingTest(private val numberBinding: NumberBinding) {

    @Test
    fun testDefaultDependencies() {
        assertTrue(this.numberBinding.dependencies.isEmpty())
    }

    protected class DoubleBindingMock : DoubleBinding() {

        override fun computeValue(): Double {
            return 0.0
        }

    }

    protected class FloatBindingMock : FloatBinding() {

        override fun computeValue(): Float {
            return 0.0f
        }

    }

    protected class IntBindingMock : IntBinding() {

        override fun computeValue(): Int {
            return 0
        }

    }

    protected class LongBindingMock : LongBinding() {

        override fun computeValue(): Long {
            return 0L
        }

    }

    protected class ShortBindingMock : ShortBinding() {

        override fun computeValue(): Short {
            return 0
        }

    }

    protected class ByteBindingMock : ByteBinding() {

        override fun computeValue(): Byte {
            return 0
        }

    }

}

class NumberBindingTest0 : NumberBindingTest(DoubleBindingMock())

class NumberBindingTest1 : NumberBindingTest(FloatBindingMock())

class NumberBindingTest2 : NumberBindingTest(LongBindingMock())

class NumberBindingTest3 : NumberBindingTest(IntBindingMock())

class NumberBindingTest4 : NumberBindingTest(ShortBindingMock())

class NumberBindingTest5 : NumberBindingTest(ByteBindingMock())