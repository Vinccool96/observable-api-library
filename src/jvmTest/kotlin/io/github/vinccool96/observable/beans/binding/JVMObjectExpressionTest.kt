package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.property.ObjectProperty
import io.github.vinccool96.observable.beans.property.SimpleObjectProperty
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class JVMObjectExpressionTest {

    private lateinit var data: Any

    private lateinit var op: ObjectProperty<Any>

    @BeforeTest
    fun setUp() {
        this.data = Any()
        this.op = SimpleObjectProperty(this.data)
    }

    @Test
    fun testAsString_Format() {
        val binding: StringBinding = this.op.asString("%h")
        DependencyUtils.checkDependencies(binding.dependencies, this.op)
        this.op.set(object : Any() {

            override fun toString(): String {
                return "foo"
            }

        })
        assertEquals(Integer.toHexString(this.op.get().hashCode()), binding.get())
    }

    @Test
    fun testAsString_Format_Locale() {
        val op: ObjectProperty<Float> = SimpleObjectProperty(1.1f)
        val binding1 = op.asString(Locale.FRENCH, "%f")
        val binding2 = op.asString(Locale.ENGLISH, "%f")
        DependencyUtils.checkDependencies(binding1.dependencies, op)
        DependencyUtils.checkDependencies(binding2.dependencies, op)
        assertNotEquals(binding1.get(), binding2.get())
    }

}