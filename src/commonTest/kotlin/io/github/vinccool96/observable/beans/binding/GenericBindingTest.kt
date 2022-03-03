@file:Suppress("unused")

package io.github.vinccool96.observable.beans.binding

import io.github.vinccool96.observable.beans.InvalidationListenerMock
import io.github.vinccool96.observable.beans.Observable
import io.github.vinccool96.observable.beans.value.ChangeListenerMock
import io.github.vinccool96.observable.beans.value.ObservableValueBase
import io.github.vinccool96.observable.collections.ObservableList
import io.github.vinccool96.observable.dev.ReturnsUnmodifiableCollection
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.fail

abstract class GenericBindingTest<T>(private val value1: T, private val value2: T,
        private val dependency1: ObservableStub,
        private val dependency2: ObservableStub, private val binding0: BindingMock<T>,
        private val binding1: BindingMock<T>, private val binding2: BindingMock<T>) {

    private lateinit var invalidationListener: InvalidationListenerMock

    private lateinit var changeListener: ChangeListenerMock<Any?>

    @BeforeTest
    fun setUp() {
        this.invalidationListener = InvalidationListenerMock()
        this.changeListener = ChangeListenerMock(UNDEFINED)
        this.binding0.value = this.value2
        this.binding1.value = this.value2
        this.binding2.value = this.value2
    }

    @AfterTest
    fun tearDown() {
        this.binding0.removeListener(this.invalidationListener)
        this.binding0.removeListener(this.changeListener)
        this.binding1.removeListener(this.invalidationListener)
        this.binding1.removeListener(this.changeListener)
        this.binding2.removeListener(this.invalidationListener)
        this.binding2.removeListener(this.changeListener)
    }

    class ObservableStub : ObservableValueBase<Any?>() {

        public override fun fireValueChangedEvent() {
            super.fireValueChangedEvent()
        }

        override val value: Any?
            get() = null

    }

    interface BindingMock<T> : Binding<T> {

        val computeValueCounter: Int

        fun reset()

        override var value: T

    }

    class DoubleBindingImpl(vararg deps: Observable) : DoubleBinding(), BindingMock<Number?> {

        private var computeValueCounterState = 0

        private var valueState = 0.0

        init {
            super.bind(*deps)
        }

        override var value: Number?
            get() = this.get()
            set(value) {
                this.valueState = value?.toDouble() ?: 0.0
            }

        override val computeValueCounter: Int
            get() {
                val result = this.computeValueCounterState
                reset()
                return result
            }

        override fun reset() {
            this.computeValueCounterState = 0
        }

        override fun computeValue(): Double {
            this.computeValueCounterState++
            return this.valueState
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = fail("Should not reach here")

    }

    class FloatBindingImpl(vararg deps: Observable) : FloatBinding(), BindingMock<Number?> {

        private var computeValueCounterState = 0

        private var valueState = 0.0f

        init {
            super.bind(*deps)
        }

        override var value: Number?
            get() = this.get()
            set(value) {
                this.valueState = value?.toFloat() ?: 0.0f
            }

        override val computeValueCounter: Int
            get() {
                val result = this.computeValueCounterState
                reset()
                return result
            }

        override fun reset() {
            this.computeValueCounterState = 0
        }

        override fun computeValue(): Float {
            this.computeValueCounterState++
            return this.valueState
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = fail("Should not reach here")

    }

    class LongBindingImpl(vararg deps: Observable) : LongBinding(), BindingMock<Number?> {

        private var computeValueCounterState = 0

        private var valueState = 0L

        init {
            super.bind(*deps)
        }

        override var value: Number?
            get() = this.get()
            set(value) {
                this.valueState = value?.toLong() ?: 0L
            }

        override val computeValueCounter: Int
            get() {
                val result = this.computeValueCounterState
                reset()
                return result
            }

        override fun reset() {
            this.computeValueCounterState = 0
        }

        override fun computeValue(): Long {
            this.computeValueCounterState++
            return this.valueState
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = fail("Should not reach here")

    }

    class IntBindingImpl(vararg deps: Observable) : IntBinding(), BindingMock<Number?> {

        private var computeValueCounterState = 0

        private var valueState = 0

        init {
            super.bind(*deps)
        }

        override var value: Number?
            get() = this.get()
            set(value) {
                this.valueState = value?.toInt() ?: 0
            }

        override val computeValueCounter: Int
            get() {
                val result = this.computeValueCounterState
                reset()
                return result
            }

        override fun reset() {
            this.computeValueCounterState = 0
        }

        override fun computeValue(): Int {
            this.computeValueCounterState++
            return this.valueState
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = fail("Should not reach here")

    }

    class ShortBindingImpl(vararg deps: Observable) : ShortBinding(), BindingMock<Number?> {

        private var computeValueCounterState = 0

        private var valueState: Short = 0

        init {
            super.bind(*deps)
        }

        override var value: Number?
            get() = this.get()
            set(value) {
                this.valueState = value?.toShort() ?: 0
            }

        override val computeValueCounter: Int
            get() {
                val result = this.computeValueCounterState
                reset()
                return result
            }

        override fun reset() {
            this.computeValueCounterState = 0
        }

        override fun computeValue(): Short {
            this.computeValueCounterState++
            return this.valueState
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = fail("Should not reach here")

    }

    class ByteBindingImpl(vararg deps: Observable) : ByteBinding(), BindingMock<Number?> {

        private var computeValueCounterState = 0

        private var valueState: Byte = 0

        init {
            super.bind(*deps)
        }

        override var value: Number?
            get() = this.get()
            set(value) {
                this.valueState = value?.toByte() ?: 0
            }

        override val computeValueCounter: Int
            get() {
                val result = this.computeValueCounterState
                reset()
                return result
            }

        override fun reset() {
            this.computeValueCounterState = 0
        }

        override fun computeValue(): Byte {
            this.computeValueCounterState++
            return this.valueState
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = fail("Should not reach here")

    }

    class BooleanBindingImpl(vararg deps: Observable) : BooleanBinding(), BindingMock<Boolean?> {

        private var computeValueCounterState = 0

        private var valueState = false

        init {
            super.bind(*deps)
        }

        override var value: Boolean?
            get() = this.get()
            set(value) {
                this.valueState = value ?: false
            }

        override val computeValueCounter: Int
            get() {
                val result = this.computeValueCounterState
                reset()
                return result
            }

        override fun reset() {
            this.computeValueCounterState = 0
        }

        override fun computeValue(): Boolean {
            this.computeValueCounterState++
            return this.valueState
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = fail("Should not reach here")

    }

    class ObjectBindingImpl(vararg deps: Observable) : ObjectBinding<Any?>(), BindingMock<Any?> {

        private var computeValueCounterState = 0

        private var valueState: Any? = null

        init {
            super.bind(*deps)
        }

        override var value: Any?
            get() = this.get()
            set(value) {
                this.valueState = value
            }

        override val computeValueCounter: Int
            get() {
                val result = this.computeValueCounterState
                reset()
                return result
            }

        override fun reset() {
            this.computeValueCounterState = 0
        }

        override fun computeValue(): Any? {
            this.computeValueCounterState++
            return this.valueState
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = fail("Should not reach here")

    }

    class StringBindingImpl(vararg deps: Observable) : StringBinding(), BindingMock<String?> {

        private var computeValueCounterState = 0

        private var valueState: String? = null

        init {
            super.bind(*deps)
        }

        override var value: String?
            get() = this.get()
            set(value) {
                this.valueState = value
            }

        override val computeValueCounter: Int
            get() {
                val result = this.computeValueCounterState
                reset()
                return result
            }

        override fun reset() {
            this.computeValueCounterState = 0
        }

        override fun computeValue(): String? {
            this.computeValueCounterState++
            return this.valueState
        }

        @get:ReturnsUnmodifiableCollection
        override val dependencies: ObservableList<Observable>
            get() = fail("Should not reach here")

    }

    companion object {

        private val UNDEFINED: Any? = null

        val dependency1 = ObservableStub()

        val dependency2 = ObservableStub()

    }

}

class GenericBindingTest0 : GenericBindingTest<Number?>(
        Double.MIN_VALUE, Double.MAX_VALUE,
        dependency1, dependency2,
        DoubleBindingImpl(),
        DoubleBindingImpl(dependency1),
        DoubleBindingImpl(dependency1, dependency2)
)

class GenericBindingTest1 : GenericBindingTest<Number?>(
        Float.MIN_VALUE, Float.MAX_VALUE,
        dependency1, dependency2,
        FloatBindingImpl(),
        FloatBindingImpl(dependency1),
        FloatBindingImpl(dependency1, dependency2)
)

class GenericBindingTest2 : GenericBindingTest<Number?>(
        Long.MIN_VALUE, Long.MAX_VALUE,
        dependency1, dependency2,
        LongBindingImpl(),
        LongBindingImpl(dependency1),
        LongBindingImpl(dependency1, dependency2)
)

class GenericBindingTest3 : GenericBindingTest<Number?>(
        Int.MIN_VALUE, Int.MAX_VALUE,
        dependency1, dependency2,
        IntBindingImpl(),
        IntBindingImpl(dependency1),
        IntBindingImpl(dependency1, dependency2)
)

class GenericBindingTest4 : GenericBindingTest<Number?>(
        Short.MIN_VALUE, Short.MAX_VALUE,
        dependency1, dependency2,
        ShortBindingImpl(),
        ShortBindingImpl(dependency1),
        ShortBindingImpl(dependency1, dependency2)
)

class GenericBindingTest5 : GenericBindingTest<Number?>(
        Byte.MIN_VALUE, Byte.MAX_VALUE,
        dependency1, dependency2,
        ByteBindingImpl(),
        ByteBindingImpl(dependency1),
        ByteBindingImpl(dependency1, dependency2)
)

class GenericBindingTest6 : GenericBindingTest<Boolean?>(
        true, false,
        dependency1, dependency2,
        BooleanBindingImpl(),
        BooleanBindingImpl(dependency1),
        BooleanBindingImpl(dependency1, dependency2)
)

class GenericBindingTest7 : GenericBindingTest<Any?>(
        Any(), Any(),
        dependency1, dependency2,
        ObjectBindingImpl(),
        ObjectBindingImpl(dependency1),
        ObjectBindingImpl(dependency1, dependency2)
)

class GenericBindingTest8 : GenericBindingTest<String?>(
        "Hello World", "Goodbye",
        dependency1, dependency2,
        StringBindingImpl(),
        StringBindingImpl(dependency1),
        StringBindingImpl(dependency1, dependency2)
)