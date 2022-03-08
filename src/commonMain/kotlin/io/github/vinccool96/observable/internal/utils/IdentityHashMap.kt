package io.github.vinccool96.observable.internal.utils

import kotlin.collections.MutableMap.MutableEntry

internal class IdentityHashMap<K, V> : AbstractMutableMap<K, V>(), MutableMap<K, V> {

    override val entries: MutableSet<MutableEntry<K, V>> by lazy {
        EntrySet()
    }

    override fun put(key: K, value: V): V? {
        return if (!(this.entries as EntrySet).backingSet.add(Container(IdentityEntry(key, value)))) {
            val c = (this.entries as EntrySet).backingSet.find { container: Container<K, V> ->
                container.entry.key === key
            }
            (c!!.entry as IdentityEntry<K, V>).setValue(value)
        } else {
            null
        }
    }

    inner class EntrySet : AbstractMutableSet<MutableEntry<K, V>>() {

        val backingSet: MutableSet<Container<K, V>> = mutableSetOf()

        override val size: Int
            get() = this.backingSet.size

        override fun add(element: MutableEntry<K, V>): Boolean {
            return this.backingSet.add(Container(element))
        }

        override fun iterator(): MutableIterator<MutableEntry<K, V>> {
            return object : MutableIterator<MutableEntry<K, V>> {

                private val backingIterator = this@EntrySet.backingSet.iterator()

                override fun hasNext(): Boolean {
                    return this.backingIterator.hasNext()
                }

                override fun next(): MutableEntry<K, V> {
                    return this.backingIterator.next().entry
                }

                override fun remove() {
                    this.backingIterator.remove()
                }

            }
        }

    }

    class Container<A, B>(val entry: MutableEntry<A, B>) {

        override fun equals(other: Any?): Boolean {
            if (other == null) {
                return false
            }
            if (this === other) {
                return true
            }
            if (other !is Container<*, *>) {
                return false
            }
            return this.entry.key === other.entry.key
        }

        override fun hashCode(): Int {
            return entry.key.hashCode()
        }

    }

    class IdentityEntry<A, B>(override val key: A, override var value: B) : MutableEntry<A, B> {

        override fun setValue(newValue: B): B {
            val old = this.value
            this.value = newValue
            return old
        }
    }

}