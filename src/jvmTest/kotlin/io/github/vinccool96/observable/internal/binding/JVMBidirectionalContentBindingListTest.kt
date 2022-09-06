package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import kotlin.test.*

class JVMBidirectionalContentBindingListTest {

    private lateinit var op2: ObservableList<Int>

    private lateinit var list0: List<Int>

    private lateinit var list1: List<Int>

    private lateinit var list2: List<Int>

    @BeforeTest
    fun setUp() {
        this.list0 = arrayListOf()
        this.list1 = arrayListOf(-1)
        this.list2 = arrayListOf(2, 1)

        this.op2 = ObservableCollections.observableArrayList(this.list2)
    }

    @Test
    @Suppress("ReplaceAssertBooleanWithAssertEquality", "UNUSED_VALUE", "KotlinConstantConditions")
    fun testEqualsWithGCedProperty() {
        var op: ObservableList<Int>? = ObservableCollections.observableArrayList(this.list1)
        val binding1 = BidirectionalContentBinding.bind(op!!, this.op2)
        BidirectionalContentBinding.unbind(op, this.op2)
        val binding2 = BidirectionalContentBinding.bind(op, this.op2)
        BidirectionalContentBinding.unbind(op, this.op2)
        val binding3 = BidirectionalContentBinding.bind(this.op2, op)
        BidirectionalContentBinding.unbind(op, this.op2)
        val binding4 = BidirectionalContentBinding.bind(this.op2, op)
        BidirectionalContentBinding.unbind(op, this.op2)
        op = null
        System.gc()
        assertTrue(binding1 == binding1)
        assertFalse(binding1 == binding2)
        assertFalse(binding1 == binding3)
        assertTrue(binding3 == binding3)
        assertFalse(binding3 == binding1)
        assertFalse(binding3 == binding4)
    }

}