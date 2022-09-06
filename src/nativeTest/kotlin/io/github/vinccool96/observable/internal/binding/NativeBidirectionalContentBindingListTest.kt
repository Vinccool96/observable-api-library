package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import kotlin.native.internal.GC
import kotlin.test.*

class NativeBidirectionalContentBindingListTest {

    private var op: ObservableList<Int>? = null

    private lateinit var op2: ObservableList<Int>

    private lateinit var list0: List<Int>

    private lateinit var list1: List<Int>

    private lateinit var list2: List<Int>

    private lateinit var binding1: Any

    private lateinit var binding2: Any

    private lateinit var binding3: Any

    private lateinit var binding4: Any

    @BeforeTest
    fun setUp() {
        this.list0 = arrayListOf()
        this.list1 = arrayListOf(-1)
        this.list2 = arrayListOf(2, 1)

        this.op2 = ObservableCollections.observableArrayList(this.list2)
    }

    @Test
    @Suppress("ReplaceAssertBooleanWithAssertEquality")
    fun testEqualsWithGCedProperty() {
        initOp()
        GC.collect()
        assertTrue(this.binding1 == this.binding1)
        assertFalse(this.binding1 == this.binding2)
        assertFalse(this.binding1 == this.binding3)
        assertTrue(this.binding3 == this.binding3)
        assertFalse(this.binding3 == this.binding1)
        assertFalse(this.binding3 == this.binding4)
    }

    private fun initOp() {
        this.op = ObservableCollections.observableArrayList(this.list1)
        this.binding1 = BidirectionalContentBinding.bind(this.op!!, this.op2)
        BidirectionalContentBinding.unbind(this.op!!, this.op2)
        this.binding2 = BidirectionalContentBinding.bind(this.op!!, this.op2)
        BidirectionalContentBinding.unbind(this.op!!, this.op2)
        this.binding3 = BidirectionalContentBinding.bind(this.op2, this.op!!)
        BidirectionalContentBinding.unbind(this.op!!, this.op2)
        this.binding4 = BidirectionalContentBinding.bind(this.op2, this.op!!)
        BidirectionalContentBinding.unbind(this.op!!, this.op2)
        this.op = null
    }

}