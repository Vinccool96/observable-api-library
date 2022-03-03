@file:Suppress("unused")

package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.property.*
import kotlin.native.internal.GC
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class NativeBidirectionalBindingTest<T>(private val factory: Factory<T>) {

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
    fun testEqualsWithGCedProperty() {
        val res = makeBindings()

        val binding1 = res[0]
        val binding2 = res[1]
        val binding3 = res[2]
        val binding4 = res[3]
        GC.collect()

        assertEquals(binding1, binding1)
        assertNotEquals(binding1, binding2)
        assertNotEquals(binding1, binding3)

        assertEquals(binding3, binding3)
        assertNotEquals(binding3, binding1)
        assertNotEquals(binding3, binding4)
    }

    @Suppress("UNUSED_VALUE")
    private fun makeBindings(): List<BidirectionalBinding<T>> {
        var p1: Property<T>? = this.factory.createProperty()
        val p2: Property<T> = this.factory.createProperty()
        p1!!.value = this.v[0]
        p2.value = this.v[1]

        val binding1 = BidirectionalBinding.bind(p1, p2)
        val binding2 = BidirectionalBinding.bind(p1, p2)
        val binding3 = BidirectionalBinding.bind(p2, p1)
        val binding4 = BidirectionalBinding.bind(p2, p1)
        p1 = null
        return listOf(binding1, binding2, binding3, binding4)
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

class NativeBidirectionalBindingTest0 :
        NativeBidirectionalBindingTest<Boolean?>(Factory({ SimpleBooleanProperty() }, booleanData))

class NativeBidirectionalBindingTest1 :
        NativeBidirectionalBindingTest<Number?>(Factory({ SimpleDoubleProperty() }, doubleData))

class NativeBidirectionalBindingTest2 :
        NativeBidirectionalBindingTest<Number?>(Factory({ SimpleFloatProperty() }, floatData))

class NativeBidirectionalBindingTest3 :
        NativeBidirectionalBindingTest<Number?>(Factory({ SimpleLongProperty() }, longData))

class NativeBidirectionalBindingTest4 :
        NativeBidirectionalBindingTest<Number?>(Factory({ SimpleIntProperty() }, intData))

class NativeBidirectionalBindingTest5 :
        NativeBidirectionalBindingTest<Number?>(Factory({ SimpleShortProperty() }, shortData))

class NativeBidirectionalBindingTest6 :
        NativeBidirectionalBindingTest<Number?>(Factory({ SimpleByteProperty() }, byteData))

class NativeBidirectionalBindingTest7 :
        NativeBidirectionalBindingTest<Any>(Factory({ SimpleObjectProperty(Any()) }, objectData))

class NativeBidirectionalBindingTest8 :
        NativeBidirectionalBindingTest<String?>(Factory({ SimpleStringProperty() }, stringData))

class NativeBidirectionalBindingTest9 :
        NativeBidirectionalBindingTest<Boolean?>(Factory({ ReadOnlyBooleanWrapper() }, booleanData))

class NativeBidirectionalBindingTest10 :
        NativeBidirectionalBindingTest<Number?>(Factory({ ReadOnlyDoubleWrapper() }, doubleData))

class NativeBidirectionalBindingTest11 :
        NativeBidirectionalBindingTest<Number?>(Factory({ ReadOnlyFloatWrapper() }, floatData))

class NativeBidirectionalBindingTest12 :
        NativeBidirectionalBindingTest<Number?>(Factory({ ReadOnlyLongWrapper() }, longData))

class NativeBidirectionalBindingTest13 :
        NativeBidirectionalBindingTest<Number?>(Factory({ ReadOnlyIntWrapper() }, intData))

class NativeBidirectionalBindingTest14 :
        NativeBidirectionalBindingTest<Number?>(Factory({ ReadOnlyShortWrapper() }, shortData))

class NativeBidirectionalBindingTest15 :
        NativeBidirectionalBindingTest<Number?>(Factory({ ReadOnlyByteWrapper() }, byteData))

class NativeBidirectionalBindingTest16 :
        NativeBidirectionalBindingTest<Any>(Factory({ ReadOnlyObjectWrapper(Any()) }, objectData))

class NativeBidirectionalBindingTest17 :
        NativeBidirectionalBindingTest<String?>(Factory({ ReadOnlyStringWrapper() }, stringData))