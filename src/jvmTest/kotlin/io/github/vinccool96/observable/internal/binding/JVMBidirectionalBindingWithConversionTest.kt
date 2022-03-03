package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.binding.Bindings
import io.github.vinccool96.observable.beans.binding.bindBidirectionalFormat
import io.github.vinccool96.observable.beans.property.Property
import io.github.vinccool96.observable.beans.property.SimpleObjectProperty
import io.github.vinccool96.observable.beans.property.SimpleStringProperty
import io.github.vinccool96.observable.beans.value.ChangeListener
import io.github.vinccool96.observable.dev.StringConverter
import java.text.DateFormat
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal abstract class JVMBidirectionalBindingWithConversionTest<S, T>(private val v0: Array<S>,
        private val v1: Array<T>) {

    abstract fun create0(): PropertyMock<S>

    abstract fun create1(): PropertyMock<T>

    abstract fun bind(obj0: PropertyMock<S>, obj1: PropertyMock<T>)

    abstract fun unbind(obj0: Any, obj1: Any)

    abstract fun check0(obj0: S, obj1: S)

    abstract fun check1(obj0: T, obj1: T)

    interface PropertyMock<T> : Property<T> {

        val listenerCount: Int

    }

    private val op0: PropertyMock<S> by lazy {
        create0()
    }

    private val op1: PropertyMock<T> by lazy {
        create1()
    }

    @BeforeTest
    fun setUp() {
        this.op0.value = this.v0[0]
        this.op1.value = this.v1[1]
    }

    @Test
    fun testBind() {
        bind(this.op0, this.op1)
        check0(this.v0[1], this.op0.value)
        check1(this.v1[1], this.op1.value)

        this.op0.value = this.v0[2]
        check0(this.v0[2], this.op0.value)
        check1(this.v1[2], this.op1.value)

        this.op1.value = this.v1[3]
        check0(this.v0[3], this.op0.value)
        check1(this.v1[3], this.op1.value)
    }

    @Test
    fun testUnbind() {
        // unbind non-existing binding => no-op
        unbind(this.op0, this.op1)

        // unbind properties of different beans
        bind(this.op0, this.op1)
        check0(this.v0[1], this.op0.value)
        check1(this.v1[1], this.op1.value)

        unbind(this.op0, this.op1)
        this.op0.value = this.v0[2]
        check0(this.v0[2], this.op0.value)
        check1(this.v1[1], this.op1.value)

        this.op1.value = this.v1[3]
        check0(this.v0[2], this.op0.value)
        check1(this.v1[3], this.op1.value)
    }

    @Test
    @Suppress("UNUSED_VALUE")
    fun testWeakReferencing() {
        var p0: PropertyMock<S>? = create0()
        var p1: PropertyMock<T>? = create1()
        p0!!.value = this.v0[0]
        p1!!.value = this.v1[1]

        bind(p0, p1)
        assertEquals(1, p0.listenerCount)
        assertEquals(1, p1.listenerCount)

        p0 = null
        System.gc()
        p1.value = this.v1[2]
        assertEquals(0, p1.listenerCount)

        p0 = create0()
        p0.value = this.v0[0]
        bind(p0, p1)
        assertEquals(1, p0.listenerCount)
        assertEquals(1, p1.listenerCount)

        p1 = null
        System.gc()
        p0.value = this.v0[3]
        assertEquals(0, p0.listenerCount)
    }

    @Test
    fun testUnbind_X_Self() {
        assertFailsWith<IllegalArgumentException> {
            unbind(this.op0, this.op0)
        }
    }

    class ObjectPropertyMock<T>(initialValue: T) : SimpleObjectProperty<T>(initialValue), PropertyMock<T> {

        private var count: Int = 0

        override val listenerCount: Int
            get() = this.count

        override fun addListener(listener: ChangeListener<in T>) {
            super.addListener(listener)
            this.count++
        }

        override fun removeListener(listener: ChangeListener<in T>) {
            super.removeListener(listener)
            this.count--
        }

    }

    class StringPropertyMock : SimpleStringProperty(), PropertyMock<String?> {

        private var count: Int = 0

        override val listenerCount: Int
            get() = this.count

        override fun addListener(listener: ChangeListener<in String?>) {
            super.addListener(listener)
            this.count++
        }

        override fun removeListener(listener: ChangeListener<in String?>) {
            super.removeListener(listener)
            this.count--
        }

    }

    companion object {

        val format: DateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US)
        val dates: Array<Date> = arrayOf(Date(), Date(0), Date(Int.MIN_VALUE.toLong()), Date(Long.MAX_VALUE))
        val strings: Array<String?> = Array(dates.size) { i: Int -> format.format(dates[i]) }

        val converter: StringConverter<Date> = object : StringConverter<Date>() {

            override fun toString(value: Date): String {
                return format.format(value)
            }

            override fun fromString(string: String): Date {
                return format.parse(string)
            }

        }
    }

}

internal class JVMBidirectionalBindingWithConversionTest0 :
        JVMBidirectionalBindingWithConversionTest<String?, Date>(strings, dates) {

    override fun create0(): PropertyMock<String?> {
        return StringPropertyMock()
    }

    override fun create1(): PropertyMock<Date> {
        return ObjectPropertyMock(Date())
    }

    override fun bind(obj0: PropertyMock<String?>, obj1: PropertyMock<Date>) {
        Bindings.bindBidirectional(obj0, obj1, converter)
    }

    override fun unbind(obj0: Any, obj1: Any) {
        Bindings.unbindBidirectional(obj0, obj1)
    }

    override fun check0(obj0: String?, obj1: String?) {
        assertEquals(obj0, obj1)
    }

    override fun check1(obj0: Date, obj1: Date) {
        assertEquals(obj0.toString(), obj1.toString())
    }

}

internal class JVMBidirectionalBindingWithConversionTest1 :
        JVMBidirectionalBindingWithConversionTest<String?, Date>(strings, dates) {

    override fun create0(): PropertyMock<String?> {
        return StringPropertyMock()
    }

    override fun create1(): PropertyMock<Date> {
        return ObjectPropertyMock(Date())
    }

    override fun bind(obj0: PropertyMock<String?>, obj1: PropertyMock<Date>) {
        Bindings.bindBidirectionalFormat(obj0, obj1, format)
    }

    override fun unbind(obj0: Any, obj1: Any) {
        Bindings.unbindBidirectional(obj0, obj1)
    }

    override fun check0(obj0: String?, obj1: String?) {
        assertEquals(obj0, obj1)
    }

    override fun check1(obj0: Date, obj1: Date) {
        assertEquals(obj0.toString(), obj1.toString())
    }

}