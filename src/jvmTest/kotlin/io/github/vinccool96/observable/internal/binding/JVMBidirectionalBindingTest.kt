package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.binding.Bindings
import io.github.vinccool96.observable.beans.property.*
import io.github.vinccool96.observable.beans.value.ObservableValue
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class JVMBidirectionalBindingTest<T>(private val factory: Factory<T>) {

    fun interface PropertyFactory<T> {

        fun createProperty(): Property<T>

    }

    class Factory<T>(private val propertyFactory: PropertyFactory<T>, val values: Array<T>) {

        fun createProperty(): Property<T> {
            return this.propertyFactory.createProperty()
        }

    }

    private lateinit var op1: Property<T>

    private lateinit var op2: Property<T>

    private lateinit var op3: Property<T>

    private lateinit var op4: Property<T>

    private lateinit var v: Array<T>

    @BeforeTest
    fun setUp() {
        this.op1 = this.factory.createProperty()
        this.op2 = this.factory.createProperty()
        this.op3 = this.factory.createProperty()
        this.op4 = this.factory.createProperty()
        this.v = this.factory.values
        this.op1.value = this.v[0]
        this.op2.value = this.v[1]
    }

    @Test
    @Suppress("UNUSED_VALUE")
    fun testWeakReferencing() {
        var p1: Property<T>? = this.factory.createProperty()
        val p2: Property<T> = this.factory.createProperty()
        var p3: Property<T>? = this.factory.createProperty()
        p1!!.value = this.v[0]
        p2.value = this.v[1]
        p3!!.value = this.v[3]

        Bindings.bindBidirectional(p1, p2)

        assertEquals(1, getListenerCount(p1))
        assertEquals(1, getListenerCount(p2))

        p1 = null
        System.gc()
        p2.value = this.v[2]
        assertEquals(0, getListenerCount(p2))

        Bindings.bindBidirectional(p2, p3)
        assertEquals(1, getListenerCount(p2))
        assertEquals(1, getListenerCount(p3))

        p3 = null
        System.gc()
        p2.value = this.v[0]
        assertEquals(0, getListenerCount(p2))
    }

    private fun getListenerCount(v: ObservableValue<T>?): Int {
        return if (v == null) -1 else ExpressionHelperUtility.getChangeListeners(v).size
    }

    @Test
    @Suppress("UNUSED_VALUE")
    fun testEqualsWithGCedProperty() {
        var p1: Property<T>? = this.factory.createProperty()
        val p2: Property<T> = this.factory.createProperty()
        p1!!.value = this.v[0]
        p2.value = this.v[1]

        val binding1 = BidirectionalBinding.bind(p1, p2)
        val binding2 = BidirectionalBinding.bind(p1, p2)
        val binding3 = BidirectionalBinding.bind(p2, p1)
        val binding4 = BidirectionalBinding.bind(p2, p1)
        p1 = null
        System.gc()

        assertEquals(binding1, binding1)
        assertNotEquals(binding1, binding2)
        assertNotEquals(binding1, binding3)

        assertEquals(binding3, binding3)
        assertNotEquals(binding3, binding1)
        assertNotEquals(binding3, binding4)
    }

    companion object {

        val booleanData: Array<Boolean?> = arrayOf(true, false, true, false)

        val doubleData: Array<Number?> = arrayOf(2348.2345, -92.214, -214.0214, -908.214)

        val floatData: Array<Number?> = arrayOf(-3592.9f, 234872.83f, 3897.274f, 3958.9387f)

        val longData: Array<Number?> = arrayOf(9823984L, 2908934L, -234234L, 9089234L)

        val intData: Array<Number?> = arrayOf(248, -9384, -234, -34)

        val shortData: Array<Number?> = arrayOf((248).toShort(), (-9384).toShort(), (-234).toShort(), (-34).toShort())

        val byteData: Array<Number?> = arrayOf((24).toByte(), (-93).toByte(), (-23).toByte(), (-34).toByte())

        val objectData: Array<Any> = arrayOf(Any(), Any(), Any(), Any())

        val stringData: Array<String?> = arrayOf("A", "B", "C", "D")

    }

}

class JVMBidirectionalBindingTest0 :
        JVMBidirectionalBindingTest<Boolean?>(Factory({ SimpleBooleanProperty() }, booleanData))

class JVMBidirectionalBindingTest1 :
        JVMBidirectionalBindingTest<Number?>(Factory({ SimpleDoubleProperty() }, doubleData))

class JVMBidirectionalBindingTest2 : JVMBidirectionalBindingTest<Number?>(Factory({ SimpleFloatProperty() }, floatData))

class JVMBidirectionalBindingTest3 : JVMBidirectionalBindingTest<Number?>(Factory({ SimpleLongProperty() }, longData))

class JVMBidirectionalBindingTest4 : JVMBidirectionalBindingTest<Number?>(Factory({ SimpleIntProperty() }, intData))

class JVMBidirectionalBindingTest5 : JVMBidirectionalBindingTest<Number?>(Factory({ SimpleShortProperty() }, shortData))

class JVMBidirectionalBindingTest6 : JVMBidirectionalBindingTest<Number?>(Factory({ SimpleByteProperty() }, byteData))

class JVMBidirectionalBindingTest7 :
        JVMBidirectionalBindingTest<Any>(Factory({ SimpleObjectProperty(Any()) }, objectData))

class JVMBidirectionalBindingTest8 :
        JVMBidirectionalBindingTest<String?>(Factory({ SimpleStringProperty() }, stringData))

class JVMBidirectionalBindingTest9 :
        JVMBidirectionalBindingTest<Boolean?>(Factory({ ReadOnlyBooleanWrapper() }, booleanData))

class JVMBidirectionalBindingTest10 :
        JVMBidirectionalBindingTest<Number?>(Factory({ ReadOnlyDoubleWrapper() }, doubleData))

class JVMBidirectionalBindingTest11 :
        JVMBidirectionalBindingTest<Number?>(Factory({ ReadOnlyFloatWrapper() }, floatData))

class JVMBidirectionalBindingTest12 : JVMBidirectionalBindingTest<Number?>(Factory({ ReadOnlyLongWrapper() }, longData))

class JVMBidirectionalBindingTest13 : JVMBidirectionalBindingTest<Number?>(Factory({ ReadOnlyIntWrapper() }, intData))

class JVMBidirectionalBindingTest14 :
        JVMBidirectionalBindingTest<Number?>(Factory({ ReadOnlyShortWrapper() }, shortData))

class JVMBidirectionalBindingTest15 : JVMBidirectionalBindingTest<Number?>(Factory({ ReadOnlyByteWrapper() }, byteData))

class JVMBidirectionalBindingTest16 :
        JVMBidirectionalBindingTest<Any>(Factory({ ReadOnlyObjectWrapper(Any()) }, objectData))

class JVMBidirectionalBindingTest17 :
        JVMBidirectionalBindingTest<String?>(Factory({ ReadOnlyStringWrapper() }, stringData))