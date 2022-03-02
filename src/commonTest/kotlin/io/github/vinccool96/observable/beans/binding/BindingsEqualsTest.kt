@file:Suppress("UNCHECKED_CAST", "unused")

package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.InvalidationListenerMock
import io.github.vinccool96.observable.beans.property.*
import io.github.vinccool96.observable.beans.value.ObservableNumberValue
import io.github.vinccool96.observable.beans.value.ObservableObjectValue
import io.github.vinccool96.observable.beans.value.ObservableStringValue
import io.github.vinccool96.observable.beans.value.ObservableValue
import kotlin.math.abs
import kotlin.native.concurrent.ThreadLocal
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class BindingsEqualsTest<T>(private val op1: ObservableValue<T>, private val op2: ObservableValue<T>,
        private val func: Functions<T>, private vararg val v: T) {

    private lateinit var observer: InvalidationListenerMock

    private lateinit var binding: BooleanBinding

    interface Functions<T> {

        fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding

        fun generateExpressionPrimitive(op1: Any, op2: T): BooleanBinding

        fun generatePrimitiveExpression(op1: T, op2: Any): BooleanBinding

        fun setOp1(value: T)

        fun setOp2(value: T)

        fun check(op1: T, op2: T, exp: BooleanBinding)

    }

    @BeforeTest
    fun setUp() {
        this.func.setOp1(this.v[0])
        this.func.setOp2(this.v[1])
        this.observer = InvalidationListenerMock()
    }

    @AfterTest
    fun tearDown() {
        this.binding.dispose()
    }

    @Test
    fun test_Expression_Expression() {
        this.binding = this.func.generateExpressionExpression(this.op1, this.op2)
        this.binding.addListener(this.observer)

        // check initial value
        this.func.check(this.v[0], this.v[1], this.binding)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op1, this.op2)

        // change first operand
        this.observer.reset()
        this.func.setOp1(this.v[1])
        this.func.check(this.v[1], this.v[1], this.binding)
        this.observer.check(this.binding, 1)

        // change second operand
        this.func.setOp2(this.v[0])
        this.func.check(this.v[1], this.v[0], this.binding)
        this.observer.check(this.binding, 1)

        // change both operands
        this.func.setOp1(this.v[0])
        this.func.setOp2(this.v[1])
        this.func.check(this.v[0], this.v[1], this.binding)
        this.observer.check(this.binding, 1)
    }

    @Test
    fun test_Self() {
        // using same FloatValue twice
        this.binding = this.func.generateExpressionExpression(this.op1, this.op1)
        this.binding.addListener(this.observer)

        // check initial value
        this.func.check(this.v[0], this.v[0], this.binding)

        // change value
        this.func.setOp1(this.v[1])
        this.func.check(this.v[1], this.v[1], this.binding)
        this.observer.check(this.binding, 1)
    }

    @Test
    fun test_Expression_Primitive() {
        this.binding = this.func.generateExpressionPrimitive(this.op1, this.v[1])
        this.binding.addListener(this.observer)

        // check initial value
        this.func.check(this.v[0], this.v[1], this.binding)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op1)

        // change first operand
        this.observer.reset()
        this.func.setOp1(this.v[1])
        this.func.check(this.v[1], this.v[1], this.binding)
        this.observer.check(this.binding, 1)

        // change to the highest value
        this.func.setOp1(this.v[2])
        this.func.check(this.v[2], this.v[1], this.binding)
        this.observer.check(this.binding, 1)
    }

    @Test
    fun test_Primitive_Expression() {
        this.binding = this.func.generatePrimitiveExpression(this.v[1], this.op1)
        this.binding.addListener(this.observer)

        // check initial value
        this.func.check(this.v[1], this.v[0], this.binding)
        DependencyUtils.checkDependencies(this.binding.dependencies, this.op1)

        // change first operand
        this.observer.reset()
        this.func.setOp1(this.v[1])
        this.func.check(this.v[1], this.v[1], this.binding)
        this.observer.check(this.binding, 1)

        // change to the highest value
        this.func.setOp1(this.v[2])
        this.func.check(this.v[1], this.v[2], this.binding)
        this.observer.check(this.binding, 1)
    }

    @ThreadLocal
    companion object {

        const val EPSILON_FLOAT: Float = 1e-5f

        const val EPSILON_DOUBLE: Double = 1e-10

        fun makeSafe(value: String?): String = value ?: ""

        val float1: FloatProperty = SimpleFloatProperty()
        val float2: FloatProperty = SimpleFloatProperty()
        val floatData = arrayOf(-EPSILON_FLOAT, 0.0f, EPSILON_FLOAT)

        val double1: DoubleProperty = SimpleDoubleProperty()
        val double2: DoubleProperty = SimpleDoubleProperty()
        val doubleData = arrayOf(-EPSILON_DOUBLE, 0.0, EPSILON_DOUBLE)

        val int1: IntProperty = SimpleIntProperty()
        val int2: IntProperty = SimpleIntProperty()
        val intData = arrayOf(-1, 0, 1)

        val long1: LongProperty = SimpleLongProperty()
        val long2: LongProperty = SimpleLongProperty()
        val longData = arrayOf(-1L, 0L, 1L)

        val short1: ShortProperty = SimpleShortProperty()
        val short2: ShortProperty = SimpleShortProperty()
        val shortData: Array<Short> = arrayOf(-1, 0, 1)

        val byte1: ByteProperty = SimpleByteProperty()
        val byte2: ByteProperty = SimpleByteProperty()
        val byteData: Array<Byte> = arrayOf(-1, 0, 1)

        val string1: StringProperty = SimpleStringProperty()
        val string2: StringProperty = SimpleStringProperty()
        val stringData = arrayOf(null, "Hello", "Hello World")
        val ciStringData = arrayOf(null, "hello", "HELLO")

        val object1: ObjectProperty<Any?> = SimpleObjectProperty(null)
        val object2: ObjectProperty<Any?> = SimpleObjectProperty(null)
        val objectData = arrayOf(Any(), Any(), Any())

    }

}

// double
class BindingsEqualsTest0 : BindingsEqualsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        EPSILON_DOUBLE)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2, EPSILON_DOUBLE)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue, EPSILON_DOUBLE)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) <= EPSILON_DOUBLE, exp.get())
            }

        },
        *doubleData)

class BindingsEqualsTest1 : BindingsEqualsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        EPSILON_DOUBLE)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2, EPSILON_DOUBLE)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue, EPSILON_DOUBLE)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) > EPSILON_DOUBLE, exp.get())
            }

        },
        *doubleData)

class BindingsEqualsTest2 : BindingsEqualsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: BooleanBinding) {
                assertEquals(op1 > op2, exp.get())
            }

        },
        *doubleData)

class BindingsEqualsTest3 : BindingsEqualsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: BooleanBinding) {
                assertEquals(op1 < op2, exp.get())
            }

        },
        *doubleData)

class BindingsEqualsTest4 : BindingsEqualsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .greaterThanOrEqual(op1 as ObservableNumberValue,
                                op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: BooleanBinding) {
                assertEquals(op1 >= op2, exp.get())
            }

        },
        *doubleData)

class BindingsEqualsTest5 : BindingsEqualsTest<Double>(
        double1 as ObservableValue<Double>, double2 as ObservableValue<Double>,
        object : Functions<Double> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .lessThanOrEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Double): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Double, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Double) {
                double1.set(value)
            }

            override fun setOp2(value: Double) {
                double2.set(value)
            }

            override fun check(op1: Double, op2: Double, exp: BooleanBinding) {
                assertEquals(op1 <= op2, exp.get())
            }

        },
        *doubleData)

// float
class BindingsEqualsTest6 : BindingsEqualsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        EPSILON_FLOAT.toDouble())
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2, EPSILON_FLOAT.toDouble())
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue, EPSILON_FLOAT.toDouble())
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) <= EPSILON_FLOAT, exp.get())
            }

        },
        *floatData)

class BindingsEqualsTest7 : BindingsEqualsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        EPSILON_FLOAT.toDouble())
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2,
                        EPSILON_FLOAT.toDouble())
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue,
                        EPSILON_FLOAT.toDouble())
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) > EPSILON_FLOAT, exp.get())
            }

        },
        *floatData)

class BindingsEqualsTest8 : BindingsEqualsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: BooleanBinding) {
                assertEquals(op1 > op2, exp.get())
            }

        },
        *floatData)

class BindingsEqualsTest9 : BindingsEqualsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: BooleanBinding) {
                assertEquals(op1 < op2, exp.get())
            }

        },
        *floatData)

class BindingsEqualsTest10 : BindingsEqualsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .greaterThanOrEqual(op1 as ObservableNumberValue,
                                op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: BooleanBinding) {
                assertEquals(op1 >= op2, exp.get())
            }

        },
        *floatData)

class BindingsEqualsTest11 : BindingsEqualsTest<Float>(
        float1 as ObservableValue<Float>, float2 as ObservableValue<Float>,
        object : Functions<Float> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Float): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Float, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Float) {
                float1.set(value)
            }

            override fun setOp2(value: Float) {
                float2.set(value)
            }

            override fun check(op1: Float, op2: Float, exp: BooleanBinding) {
                assertEquals(op1 <= op2, exp.get())
            }

        },
        *floatData)

// int
class BindingsEqualsTest12 : BindingsEqualsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: BooleanBinding) {
                assertEquals(op1 == op2, exp.get())
            }

        },
        *intData)

class BindingsEqualsTest13 : BindingsEqualsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        1.0)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2, 1.0)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue, 1.0)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) <= 1, exp.get())
            }

        },
        *intData)

class BindingsEqualsTest14 : BindingsEqualsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: BooleanBinding) {
                assertEquals(op1 != op2, exp.get())
            }

        },
        *intData)

class BindingsEqualsTest15 : BindingsEqualsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        1.0)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2, 1.0)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue, 1.0)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) > 1, exp.get())
            }

        },
        *intData)

class BindingsEqualsTest16 : BindingsEqualsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: BooleanBinding) {
                assertEquals(op1 > op2, exp.get())
            }

        },
        *intData)

class BindingsEqualsTest17 : BindingsEqualsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: BooleanBinding) {
                assertEquals(op1 < op2, exp.get())
            }

        },
        *intData)

class BindingsEqualsTest18 : BindingsEqualsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .greaterThanOrEqual(op1 as ObservableNumberValue,
                                op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: BooleanBinding) {
                assertEquals(op1 >= op2, exp.get())
            }

        },
        *intData)

class BindingsEqualsTest19 : BindingsEqualsTest<Int>(
        int1 as ObservableValue<Int>, int2 as ObservableValue<Int>,
        object : Functions<Int> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .lessThanOrEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Int): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Int, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Int) {
                int1.set(value)
            }

            override fun setOp2(value: Int) {
                int2.set(value)
            }

            override fun check(op1: Int, op2: Int, exp: BooleanBinding) {
                assertEquals(op1 <= op2, exp.get())
            }

        },
        *intData)

// long
class BindingsEqualsTest20 : BindingsEqualsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: BooleanBinding) {
                assertEquals(op1 == op2, exp.get())
            }

        },
        *longData)

class BindingsEqualsTest21 : BindingsEqualsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        1.0)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2, 1.0)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue, 1.0)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) <= 1, exp.get())
            }

        },
        *longData)

class BindingsEqualsTest22 : BindingsEqualsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: BooleanBinding) {
                assertEquals(op1 != op2, exp.get())
            }

        },
        *longData)

class BindingsEqualsTest23 : BindingsEqualsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        1.0)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2, 1.0)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue, 1.0)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) > 1, exp.get())
            }

        },
        *longData)

class BindingsEqualsTest24 : BindingsEqualsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: BooleanBinding) {
                assertEquals(op1 > op2, exp.get())
            }

        },
        *longData)

class BindingsEqualsTest25 : BindingsEqualsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: BooleanBinding) {
                assertEquals(op1 < op2, exp.get())
            }

        },
        *longData)

class BindingsEqualsTest26 : BindingsEqualsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .greaterThanOrEqual(op1 as ObservableNumberValue,
                                op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: BooleanBinding) {
                assertEquals(op1 >= op2, exp.get())
            }

        },
        *longData)

class BindingsEqualsTest27 : BindingsEqualsTest<Long>(
        long1 as ObservableValue<Long>, long2 as ObservableValue<Long>,
        object : Functions<Long> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Long): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Long, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Long) {
                long1.set(value)
            }

            override fun setOp2(value: Long) {
                long2.set(value)
            }

            override fun check(op1: Long, op2: Long, exp: BooleanBinding) {
                assertEquals(op1 <= op2, exp.get())
            }

        },
        *longData)

// short
class BindingsEqualsTest28 : BindingsEqualsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: BooleanBinding) {
                assertEquals(op1 == op2, exp.get())
            }

        },
        *shortData)

class BindingsEqualsTest29 : BindingsEqualsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        1.0)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2, 1.0)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue, 1.0)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) <= 1, exp.get())
            }

        },
        *shortData)

class BindingsEqualsTest30 : BindingsEqualsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: BooleanBinding) {
                assertEquals(op1 != op2, exp.get())
            }

        },
        *shortData)

class BindingsEqualsTest31 : BindingsEqualsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        1.0)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2, 1.0)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue, 1.0)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) > 1, exp.get())
            }

        },
        *shortData)

class BindingsEqualsTest32 : BindingsEqualsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: BooleanBinding) {
                assertEquals(op1 > op2, exp.get())
            }

        },
        *shortData)

class BindingsEqualsTest33 : BindingsEqualsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: BooleanBinding) {
                assertEquals(op1 < op2, exp.get())
            }

        },
        *shortData)

class BindingsEqualsTest34 : BindingsEqualsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .greaterThanOrEqual(op1 as ObservableNumberValue,
                                op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: BooleanBinding) {
                assertEquals(op1 >= op2, exp.get())
            }

        },
        *shortData)

class BindingsEqualsTest35 : BindingsEqualsTest<Short>(
        short1 as ObservableValue<Short>, short2 as ObservableValue<Short>,
        object : Functions<Short> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Short): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Short, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Short) {
                short1.set(value)
            }

            override fun setOp2(value: Short) {
                short2.set(value)
            }

            override fun check(op1: Short, op2: Short, exp: BooleanBinding) {
                assertEquals(op1 <= op2, exp.get())
            }

        },
        *shortData)

// byte
class BindingsEqualsTest36 : BindingsEqualsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: BooleanBinding) {
                assertEquals(op1 == op2, exp.get())
            }

        },
        *byteData)

class BindingsEqualsTest37 : BindingsEqualsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        1.0)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): BooleanBinding {
                return Bindings.equal(op1 as ObservableNumberValue, op2, 1.0)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableNumberValue, 1.0)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) <= 1, exp.get())
            }

        },
        *byteData)

class BindingsEqualsTest38 : BindingsEqualsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: BooleanBinding) {
                assertEquals(op1 != op2, exp.get())
            }

        },
        *byteData)

class BindingsEqualsTest39 : BindingsEqualsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2 as ObservableNumberValue,
                        1.0)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableNumberValue, op2, 1.0)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableNumberValue, 1.0)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: BooleanBinding) {
                assertEquals(abs(op1 - op2) > 1, exp.get())
            }

        },
        *byteData)

class BindingsEqualsTest40 : BindingsEqualsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: BooleanBinding) {
                assertEquals(op1 > op2, exp.get())
            }

        },
        *byteData)

class BindingsEqualsTest41 : BindingsEqualsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: BooleanBinding) {
                assertEquals(op1 < op2, exp.get())
            }

        },
        *byteData)

class BindingsEqualsTest42 : BindingsEqualsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .greaterThanOrEqual(op1 as ObservableNumberValue,
                                op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: BooleanBinding) {
                assertEquals(op1 >= op2, exp.get())
            }

        },
        *byteData)

class BindingsEqualsTest43 : BindingsEqualsTest<Byte>(
        byte1 as ObservableValue<Byte>, byte2 as ObservableValue<Byte>,
        object : Functions<Byte> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue,
                        op2 as ObservableNumberValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Byte): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableNumberValue, op2)
            }

            override fun generatePrimitiveExpression(op1: Byte, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1, op2 as ObservableNumberValue)
            }

            override fun setOp1(value: Byte) {
                byte1.set(value)
            }

            override fun setOp2(value: Byte) {
                byte2.set(value)
            }

            override fun check(op1: Byte, op2: Byte, exp: BooleanBinding) {
                assertEquals(op1 <= op2, exp.get())
            }

        },
        *byteData)

// string
class BindingsEqualsTest44 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableStringValue, op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.equal(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1) == makeSafe(op2), exp.get())
            }

        },
        *stringData)

class BindingsEqualsTest45 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableStringValue, op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.equal(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1) == makeSafe(op2), exp.get())
            }

        },
        *ciStringData)

class BindingsEqualsTest46 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .equalIgnoreCase(op1 as ObservableStringValue, op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.equalIgnoreCase(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.equalIgnoreCase(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1).equals(makeSafe(op2), ignoreCase = true), exp.get())
            }

        },
        *stringData)

class BindingsEqualsTest47 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .equalIgnoreCase(op1 as ObservableStringValue, op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.equalIgnoreCase(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.equalIgnoreCase(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1).equals(makeSafe(op2), ignoreCase = true), exp.get())
            }

        },
        *ciStringData)

class BindingsEqualsTest48 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableStringValue, op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1) != makeSafe(op2), exp.get())
            }

        },
        *stringData)

class BindingsEqualsTest49 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableStringValue, op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1) != makeSafe(op2), exp.get())
            }

        },
        *ciStringData)

class BindingsEqualsTest50 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .notEqualIgnoreCase(op1 as ObservableStringValue,
                                op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.notEqualIgnoreCase(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.notEqualIgnoreCase(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(!makeSafe(op1).equals(makeSafe(op2), ignoreCase = true), exp.get())
            }

        },
        *stringData)

class BindingsEqualsTest51 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .notEqualIgnoreCase(op1 as ObservableStringValue,
                                op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.notEqualIgnoreCase(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.notEqualIgnoreCase(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(!makeSafe(op1).equals(makeSafe(op2), ignoreCase = true), exp.get())
            }

        },
        *ciStringData)

class BindingsEqualsTest52 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableStringValue,
                        op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.greaterThan(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.greaterThan(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1) > makeSafe(op2), exp.get())
            }

        },
        *stringData)

class BindingsEqualsTest53 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableStringValue, op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.lessThan(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.lessThan(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1) < makeSafe(op2), exp.get())
            }

        },
        *stringData)

class BindingsEqualsTest54 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .greaterThanOrEqual(op1 as ObservableStringValue,
                                op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.greaterThanOrEqual(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1) >= makeSafe(op2), exp.get())
            }

        },
        *stringData)

class BindingsEqualsTest55 : BindingsEqualsTest<String?>(
        string1, string2,
        object : Functions<String?> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings
                        .lessThanOrEqual(op1 as ObservableStringValue, op2 as ObservableStringValue)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: String?): BooleanBinding {
                return Bindings.lessThanOrEqual(op1 as ObservableStringValue, op2)
            }

            override fun generatePrimitiveExpression(op1: String?, op2: Any): BooleanBinding {
                return Bindings.lessThanOrEqual(op1, op2 as ObservableStringValue)
            }

            override fun setOp1(value: String?) {
                string1.set(value)
            }

            override fun setOp2(value: String?) {
                string2.set(value)
            }

            override fun check(op1: String?, op2: String?, exp: BooleanBinding) {
                assertEquals(makeSafe(op1) <= makeSafe(op2), exp.get())
            }

        },
        *stringData)

// Any
class BindingsEqualsTest56 : BindingsEqualsTest<Any>(
        object1 as ObservableValue<Any>, object2 as ObservableValue<Any>,
        object : Functions<Any> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableObjectValue<Any>,
                        op2 as ObservableObjectValue<Any>)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1 as ObservableObjectValue<Any>, op2)
            }

            override fun generatePrimitiveExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.equal(op1, op2 as ObservableObjectValue<Any>)
            }

            override fun setOp1(value: Any) {
                object1.set(value)
            }

            override fun setOp2(value: Any) {
                object2.set(value)
            }

            override fun check(op1: Any, op2: Any, exp: BooleanBinding) {
                assertEquals(op1 == op2, exp.get())
            }

        },
        *objectData)

class BindingsEqualsTest57 : BindingsEqualsTest<Any>(
        object1 as ObservableValue<Any>, object2 as ObservableValue<Any>,
        object : Functions<Any> {

            override fun generateExpressionExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableObjectValue<Any>,
                        op2 as ObservableObjectValue<Any>)
            }

            override fun generateExpressionPrimitive(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1 as ObservableObjectValue<Any>, op2)
            }

            override fun generatePrimitiveExpression(op1: Any, op2: Any): BooleanBinding {
                return Bindings.notEqual(op1, op2 as ObservableObjectValue<Any>)
            }

            override fun setOp1(value: Any) {
                object1.set(value)
            }

            override fun setOp2(value: Any) {
                object2.set(value)
            }

            override fun check(op1: Any, op2: Any, exp: BooleanBinding) {
                assertEquals(op1 != op2, exp.get())
            }

        },
        *objectData)