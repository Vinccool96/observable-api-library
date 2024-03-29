package io.github.vinccool96.observable.beans.property

import io.github.vinccool96.observable.beans.InvalidationListener
import io.github.vinccool96.observable.beans.value.ChangeListener
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class ReadOnlyDoublePropertyTest {

    @Test
    fun testToString() {
        val v1: ReadOnlyDoubleProperty = ReadOnlyDoublePropertyStub(NO_BEAN, NO_NAME_1)
        assertEquals("ReadOnlyDoubleProperty [value: 0.0]", v1.toString())

        val v2: ReadOnlyDoubleProperty = ReadOnlyDoublePropertyStub(NO_BEAN, NO_NAME_2)
        assertEquals("ReadOnlyDoubleProperty [value: 0.0]", v2.toString())

        val bean = Any()
        val name = "General Kenobi"

        val v3: ReadOnlyDoubleProperty = ReadOnlyDoublePropertyStub(bean, name)
        assertEquals("ReadOnlyDoubleProperty [bean: $bean, name: General Kenobi, value: 0.0]", v3.toString())

        val v4: ReadOnlyDoubleProperty = ReadOnlyDoublePropertyStub(bean, NO_NAME_1)
        assertEquals("ReadOnlyDoubleProperty [bean: $bean, value: 0.0]", v4.toString())

        val v5: ReadOnlyDoubleProperty = ReadOnlyDoublePropertyStub(bean, NO_NAME_2)
        assertEquals("ReadOnlyDoubleProperty [bean: $bean, value: 0.0]", v5.toString())

        val v6: ReadOnlyDoubleProperty = ReadOnlyDoublePropertyStub(NO_BEAN, name)
        assertEquals("ReadOnlyDoubleProperty [name: General Kenobi, value: 0.0]", v6.toString())
    }

    @Test
    fun testAsObject() {
        val valueModel = ReadOnlyDoubleWrapper()
        val exp: ReadOnlyObjectProperty<Double> = valueModel.readOnlyProperty.asObject()
        assertNull(exp.bean)
        assertSame(valueModel.name, exp.name)

        assertEquals(0.0, exp.get(), EPSILON)
        valueModel.set(-4354.3)
        assertEquals(-4354.3, exp.get(), EPSILON)
        valueModel.set(5e11)
        assertEquals(5e11, exp.get(), EPSILON)
    }

    @Test
    fun testObjectToDouble() {
        val valueModel: ReadOnlyObjectWrapper<Double?> = ReadOnlyObjectWrapper(null)
        val exp: ReadOnlyDoubleProperty = ReadOnlyDoubleProperty.readOnlyDoubleProperty(valueModel.readOnlyProperty)
        assertNull(exp.bean)
        assertSame(valueModel.name, exp.name)

        assertEquals(0.0, exp.get(), EPSILON)
        valueModel.set(-4354.3)
        assertEquals(-4354.3, exp.get(), EPSILON)
        valueModel.set(5e11)
        assertEquals(5e11, exp.get(), EPSILON)
    }

    private class ReadOnlyDoublePropertyStub(override val bean: Any?, override val name: String?) :
            ReadOnlyDoubleProperty() {

        override fun get(): Double {
            return 0.0
        }

        override fun addListener(listener: InvalidationListener) {
        }

        override fun removeListener(listener: InvalidationListener) {
        }

        override fun hasListener(listener: InvalidationListener): Boolean {
            return false
        }

        override fun addListener(listener: ChangeListener<in Number?>) {
        }

        override fun removeListener(listener: ChangeListener<in Number?>) {
        }

        override fun hasListener(listener: ChangeListener<in Number?>): Boolean {
            return false
        }

    }

    companion object {

        private const val EPSILON: Double = 1e-12

        private val NO_BEAN: Any? = null

        private val NO_NAME_1: String? = null

        private const val NO_NAME_2: String = ""

    }

}