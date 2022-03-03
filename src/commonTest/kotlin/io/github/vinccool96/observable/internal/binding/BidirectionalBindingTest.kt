@file:Suppress("unused")

package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.binding.Bindings
import io.github.vinccool96.observable.beans.property.*
import kotlin.test.*

abstract class BidirectionalBindingTest<T>(private val factory: Factory<T>) {

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
    fun testBind() {
        Bindings.bindBidirectional(this.op1, this.op2)
        Bindings.bindBidirectional(this.op1, this.op2)
        assertEquals(this.v[1], this.op1.value)
        assertEquals(this.v[1], this.op2.value)

        this.op1.value = this.v[2]
        assertEquals(this.v[2], this.op1.value)
        assertEquals(this.v[2], this.op2.value)

        this.op2.value = this.v[3]
        assertEquals(this.v[3], this.op1.value)
        assertEquals(this.v[3], this.op2.value)
    }

    @Test
    fun testUnbind() {
        // unbind non-existing binding => no-op
        Bindings.unbindBidirectional(this.op1, this.op2)

        // unbind properties of different beans
        Bindings.bindBidirectional(this.op1, this.op2)
        assertEquals(this.v[1], this.op1.value)
        assertEquals(this.v[1], this.op2.value)

        Bindings.unbindBidirectional(this.op1, this.op2)
        this.op1.value = this.v[2]
        assertEquals(this.v[2], this.op1.value)
        assertEquals(this.v[1], this.op2.value)

        this.op2.value = this.v[3]
        assertEquals(this.v[2], this.op1.value)
        assertEquals(this.v[3], this.op2.value)
    }

    @Test
    fun testChaining() {
        this.op3.value = this.v[2]
        Bindings.bindBidirectional(this.op1, this.op2)
        Bindings.bindBidirectional(this.op2, this.op3)
        assertEquals(this.v[2], this.op1.value)
        assertEquals(this.v[2], this.op2.value)
        assertEquals(this.v[2], this.op3.value)

        this.op1.value = this.v[3]
        assertEquals(this.v[3], this.op1.value)
        assertEquals(this.v[3], this.op2.value)
        assertEquals(this.v[3], this.op3.value)

        this.op2.value = this.v[0]
        assertEquals(this.v[0], this.op1.value)
        assertEquals(this.v[0], this.op2.value)
        assertEquals(this.v[0], this.op3.value)

        this.op3.value = this.v[1]
        assertEquals(this.v[1], this.op1.value)
        assertEquals(this.v[1], this.op2.value)
        assertEquals(this.v[1], this.op3.value)

        // now unbind
        Bindings.unbindBidirectional(this.op1, this.op2)
        assertEquals(this.v[1], this.op1.value)
        assertEquals(this.v[1], this.op2.value)
        assertEquals(this.v[1], this.op3.value)

        this.op1.value = this.v[2]
        assertEquals(this.v[2], this.op1.value)
        assertEquals(this.v[1], this.op2.value)
        assertEquals(this.v[1], this.op3.value)

        this.op2.value = this.v[3]
        assertEquals(this.v[2], this.op1.value)
        assertEquals(this.v[3], this.op2.value)
        assertEquals(this.v[3], this.op3.value)

        this.op3.value = this.v[0]
        assertEquals(this.v[2], this.op1.value)
        assertEquals(this.v[0], this.op2.value)
        assertEquals(this.v[0], this.op3.value)
    }

    @Test
    fun testHashCode() {
        val hc1: Int = BidirectionalBinding.bind(this.op1, this.op2).hashCode()
        val hc2: Int = BidirectionalBinding.bind(this.op2, this.op1).hashCode()
        assertEquals(hc1, hc2)
    }

    @Test
    fun testEquals() {
        val golden: BidirectionalBinding<T> = BidirectionalBinding.bind(this.op1, this.op2)

        assertEquals(golden, golden)
        assertNotEquals(golden as Any, this.op1)
        assertEquals(golden, BidirectionalBinding.bind(this.op1, this.op2))
        assertEquals(golden, BidirectionalBinding.bind(this.op2, this.op1))
        assertNotEquals(golden, BidirectionalBinding.bind(this.op1, this.op3))
        assertNotEquals(golden, BidirectionalBinding.bind(this.op3, this.op1))
        assertNotEquals(golden, BidirectionalBinding.bind(this.op3, this.op2))
        assertNotEquals(golden, BidirectionalBinding.bind(this.op2, this.op3))
    }

    @Test
    fun testBind_X_Self() {
        assertFailsWith<IllegalArgumentException> {
            Bindings.bindBidirectional(this.op1, this.op1)
        }
    }

    @Test
    fun testUnbind_X_Self() {
        assertFailsWith<IllegalArgumentException> {
            Bindings.unbindBidirectional(this.op1, this.op1)
        }
    }

    @Test
    fun testBrokenBind() {
        Bindings.bindBidirectional(this.op1, this.op2)
        this.op1.bind(this.op3)
        assertEquals(this.op3.value, this.op1.value)
        assertEquals(this.op2.value, this.op1.value)

        this.op2.value = this.v[2]
        assertEquals(this.op3.value, this.op1.value)
        assertEquals(this.op2.value, this.op1.value)
    }

    @Test
    fun testDoubleBrokenBind() {
        Bindings.bindBidirectional(this.op1, this.op2)
        this.op1.bind(this.op3)
        this.op4.value = this.v[0]

        this.op2.bind(this.op4)
        assertEquals(this.op4.value, this.op2.value)
        assertEquals(this.op3.value, this.op1.value)
        // Test that bidirectional binding was unbound in this case
        this.op3.value = this.v[0]
        this.op4.value = this.v[1]
        assertEquals(this.op4.value, this.op2.value)
        assertEquals(this.op3.value, this.op1.value)
        assertEquals(this.v[0], this.op1.value)
        assertEquals(this.v[1], this.op2.value)
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

class BidirectionalBindingTest0 : BidirectionalBindingTest<Boolean?>(Factory({ SimpleBooleanProperty() }, booleanData))

class BidirectionalBindingTest1 : BidirectionalBindingTest<Number?>(Factory({ SimpleDoubleProperty() }, doubleData))

class BidirectionalBindingTest2 : BidirectionalBindingTest<Number?>(Factory({ SimpleFloatProperty() }, floatData))

class BidirectionalBindingTest3 : BidirectionalBindingTest<Number?>(Factory({ SimpleLongProperty() }, longData))

class BidirectionalBindingTest4 : BidirectionalBindingTest<Number?>(Factory({ SimpleIntProperty() }, intData))

class BidirectionalBindingTest5 : BidirectionalBindingTest<Number?>(Factory({ SimpleShortProperty() }, shortData))

class BidirectionalBindingTest6 : BidirectionalBindingTest<Number?>(Factory({ SimpleByteProperty() }, byteData))

class BidirectionalBindingTest7 : BidirectionalBindingTest<Any>(Factory({ SimpleObjectProperty(Any()) }, objectData))

class BidirectionalBindingTest8 : BidirectionalBindingTest<String?>(Factory({ SimpleStringProperty() }, stringData))

class BidirectionalBindingTest9 : BidirectionalBindingTest<Boolean?>(Factory({ ReadOnlyBooleanWrapper() }, booleanData))

class BidirectionalBindingTest10 : BidirectionalBindingTest<Number?>(Factory({ ReadOnlyDoubleWrapper() }, doubleData))

class BidirectionalBindingTest11 : BidirectionalBindingTest<Number?>(Factory({ ReadOnlyFloatWrapper() }, floatData))

class BidirectionalBindingTest12 : BidirectionalBindingTest<Number?>(Factory({ ReadOnlyLongWrapper() }, longData))

class BidirectionalBindingTest13 : BidirectionalBindingTest<Number?>(Factory({ ReadOnlyIntWrapper() }, intData))

class BidirectionalBindingTest14 : BidirectionalBindingTest<Number?>(Factory({ ReadOnlyShortWrapper() }, shortData))

class BidirectionalBindingTest15 : BidirectionalBindingTest<Number?>(Factory({ ReadOnlyByteWrapper() }, byteData))

class BidirectionalBindingTest16 : BidirectionalBindingTest<Any>(Factory({ ReadOnlyObjectWrapper(Any()) }, objectData))

class BidirectionalBindingTest17 : BidirectionalBindingTest<String?>(Factory({ ReadOnlyStringWrapper() }, stringData))
