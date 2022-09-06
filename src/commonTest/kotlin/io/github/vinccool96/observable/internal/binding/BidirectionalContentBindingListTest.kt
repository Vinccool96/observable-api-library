package io.github.vinccool96.observable.internal.binding

import io.github.vinccool96.observable.beans.binding.Bindings
import io.github.vinccool96.observable.collections.ObservableCollections
import io.github.vinccool96.observable.collections.ObservableList
import kotlin.test.*

@Suppress("ReplaceAssertBooleanWithAssertEquality", "SENSELESS_COMPARISON")
class BidirectionalContentBindingListTest {

    private lateinit var op1: ObservableList<Int>

    private lateinit var op2: ObservableList<Int>

    private lateinit var op3: ObservableList<Int>

    private lateinit var list0: List<Int>

    private lateinit var list1: List<Int>

    private lateinit var list2: List<Int>

    @BeforeTest
    fun setUp() {
        this.list0 = arrayListOf()
        this.list1 = arrayListOf(-1)
        this.list2 = arrayListOf(2, 1)

        this.op1 = ObservableCollections.observableArrayList(this.list1)
        this.op2 = ObservableCollections.observableArrayList(this.list2)
        this.op3 = ObservableCollections.observableArrayList(this.list0)
    }

    @Test
    fun testBind() {
        val list2_sorted: List<Int> = arrayListOf(1, 2)
        Bindings.bindContentBidirectional(this.op1, this.op2)

        assertEquals(this.list2, this.op1)
        assertEquals(this.list2, this.op2)

        this.op1.setAll(this.list1)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list1, this.op2)

        this.op1.setAll(this.list0)
        assertEquals(this.list0, this.op1)
        assertEquals(this.list0, this.op2)

        this.op1.setAll(this.list2)
        assertEquals(this.list2, this.op1)
        assertEquals(this.list2, this.op2)

        ObservableCollections.sort(this.op1)
        assertEquals(list2_sorted, this.op1)
        assertEquals(list2_sorted, this.op2)

        this.op2.setAll(this.list1)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list1, this.op2)

        this.op2.setAll(this.list0)
        assertEquals(this.list0, this.op1)
        assertEquals(this.list0, this.op2)

        this.op2.setAll(this.list2)
        assertEquals(this.list2, this.op1)
        assertEquals(this.list2, this.op2)

        ObservableCollections.sort(this.op2)
        assertEquals(list2_sorted, this.op1)
        assertEquals(list2_sorted, this.op2)
    }

    @Test
    fun testBind_X_Self() {
        assertFailsWith<IllegalArgumentException> {
            Bindings.bindContentBidirectional(this.op1, this.op1)
        }
    }

    @Test
    fun testUnbind() {
        // unbind non-existing binding => no-op
        Bindings.unbindContentBidirectional(this.op1, this.op2)

        Bindings.bindContentBidirectional(this.op1, this.op2)

        assertEquals(this.list2, this.op1)
        assertEquals(this.list2, this.op2)

        Bindings.unbindContentBidirectional(this.op1, this.op2)
        assertEquals(this.list2, this.op1)
        assertEquals(this.list2, this.op2)

        this.op1.setAll(this.list1)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list2, this.op2)

        this.op2.setAll(this.list0)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list0, this.op2)

        // unbind in flipped order
        Bindings.bindContentBidirectional(this.op1, this.op2)

        assertEquals(this.list0, this.op1)
        assertEquals(this.list0, this.op2)

        Bindings.unbindContentBidirectional(this.op2, this.op1)
        assertEquals(this.list0, this.op1)
        assertEquals(this.list0, this.op2)

        this.op1.setAll(this.list1)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list0, this.op2)

        this.op2.setAll(this.list2)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list2, this.op2)
    }

    @Test
    fun testUnbind_X_Self() {
        assertFailsWith<IllegalArgumentException> {
            Bindings.unbindContentBidirectional(this.op1, this.op1)
        }
    }

    @Test
    fun testChaining() {
        Bindings.bindContentBidirectional(this.op1, this.op2)
        Bindings.bindContentBidirectional(this.op2, this.op3)
        assertEquals(this.list0, this.op1)
        assertEquals(this.list0, this.op2)
        assertEquals(this.list0, this.op3)
        this.op1.setAll(this.list1)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list1, this.op2)
        assertEquals(this.list1, this.op3)
        this.op2.setAll(this.list2)
        assertEquals(this.list2, this.op1)
        assertEquals(this.list2, this.op2)
        assertEquals(this.list2, this.op3)
        this.op3.setAll(this.list0)
        assertEquals(this.list0, this.op1)
        assertEquals(this.list0, this.op2)
        assertEquals(this.list0, this.op3)

        // now unbind
        Bindings.unbindContentBidirectional(this.op1, this.op2)
        assertEquals(this.list0, this.op1)
        assertEquals(this.list0, this.op2)
        assertEquals(this.list0, this.op3)
        this.op1.setAll(this.list1)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list0, this.op2)
        assertEquals(this.list0, this.op3)
        this.op2.setAll(this.list2)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list2, this.op2)
        assertEquals(this.list2, this.op3)
        this.op3.setAll(this.list0)
        assertEquals(this.list1, this.op1)
        assertEquals(this.list0, this.op2)
        assertEquals(this.list0, this.op3)
    }

    @Test
    fun testHashCode() {
        val hc1 = BidirectionalContentBinding.bind(this.op1, this.op2).hashCode()
        BidirectionalContentBinding.unbind(this.op1, this.op2)
        val hc2 = BidirectionalContentBinding.bind(this.op2, this.op1).hashCode()
        assertEquals(hc1.toLong(), hc2.toLong())
    }

    @Test
    fun testEquals() {
        val golden = BidirectionalContentBinding.bind(this.op1, this.op2)
        BidirectionalContentBinding.unbind(this.op1, this.op2)
        assertTrue(golden == golden)
        assertFalse(golden == null)
        assertFalse(golden == this.op1)
        assertTrue(golden == BidirectionalContentBinding.bind(this.op1, this.op2))
        BidirectionalContentBinding.unbind(this.op1, this.op2)
        assertTrue(golden == BidirectionalContentBinding.bind(this.op2, this.op1))
        BidirectionalContentBinding.unbind(this.op1, this.op2)
        assertFalse(golden == BidirectionalContentBinding.bind(this.op1, this.op3))
        BidirectionalContentBinding.unbind(this.op1, this.op3)
        assertFalse(golden == BidirectionalContentBinding.bind(this.op3, this.op1))
        BidirectionalContentBinding.unbind(this.op1, this.op3)
        assertFalse(golden == BidirectionalContentBinding.bind(this.op3, this.op2))
        BidirectionalContentBinding.unbind(this.op2, this.op3)
        assertFalse(golden == BidirectionalContentBinding.bind(this.op2, this.op3))
        BidirectionalContentBinding.unbind(this.op2, this.op3)
    }

}