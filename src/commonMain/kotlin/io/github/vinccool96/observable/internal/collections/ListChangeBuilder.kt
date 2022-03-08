package io.github.vinccool96.observable.internal.collections

import io.github.vinccool96.observable.collections.ListChangeListener.Change
import io.github.vinccool96.observable.collections.ObservableList
import io.github.vinccool96.observable.collections.ObservableListBase
import io.github.vinccool96.observable.internal.collections.ChangeHelper.addRemoveChangeToString
import io.github.vinccool96.observable.internal.collections.ChangeHelper.permChangeToString
import io.github.vinccool96.observable.internal.collections.ChangeHelper.updateChangeToString

@Suppress("UNCHECKED_CAST", "SENSELESS_COMPARISON")
internal class ListChangeBuilder<E>(private val list: ObservableListBase<E>) : ChangeBuilder<E>() {

    override val size: Int
        get() = this.list.size

    override fun commit() {
        val addRemoveNotEmpty = this.addRemoveChanges.isNotEmpty()
        val updateNotEmpty = this.updateChanges.isNotEmpty()
        if (this.changeLock == 0 && (addRemoveNotEmpty || updateNotEmpty || this.permutationChange != null)) {
            var totalSize =
                    this.updateChanges.size + this.addRemoveChanges.size + if (this.permutationChange != null) 1 else 0
            if (totalSize == 1) {
                if (addRemoveNotEmpty) {
                    this.list.fireChanges(
                            SingleChange(finalizeSubChange(this.addRemoveChanges[0]), list) as Change<out E>)
                    this.addRemoveChanges.clear()
                } else if (updateNotEmpty) {
                    this.list.fireChanges(SingleChange(finalizeSubChange(this.updateChanges[0]), list))
                    this.updateChanges.clear()
                } else {
                    this.list.fireChanges(SingleChange(finalizeSubChange(this.permutationChange!!), list))
                    this.permutationChange = null
                }
            } else {
                if (updateNotEmpty) {
                    val removed = compress(this.updateChanges as MutableList<SubChange<E>?>)
                    totalSize -= removed
                }
                if (addRemoveNotEmpty) {
                    val removed = compress(this.addRemoveChanges as MutableList<SubChange<E>?>)
                    totalSize -= removed
                }
                val array: Array<SubChange<E>?> = arrayOfNulls(totalSize)
                var ptr = 0
                if (this.permutationChange != null) {
                    array[ptr++] = this.permutationChange
                }
                if (addRemoveNotEmpty) {
                    val sz = this.addRemoveChanges.size
                    for (i in 0 until sz) {
                        val change = this.addRemoveChanges[i]
                        if (change != null) {
                            array[ptr++] = change
                        }
                    }
                }
                if (updateNotEmpty) {
                    val sz = this.updateChanges.size
                    for (i in 0 until sz) {
                        val change = this.updateChanges[i]
                        if (change != null) {
                            array[ptr++] = change
                        }
                    }
                }
                this.list.fireChanges(IterableChange(finalizeSubChangeArray(array as Array<SubChange<E>>), this.list))
                this.addRemoveChanges.clear()
                if (this.updateChanges != null) {
                    this.updateChanges.clear()
                }
                this.permutationChange = null
            }
        }
    }

    private class SingleChange<E>(private val change: SubChange<E>, list: ObservableListBase<E>) : Change<E>(list) {

        private var onChange: Boolean = false

        override fun next(): Boolean {
            return if (this.onChange) {
                false
            } else {
                this.onChange = true
                true
            }
        }

        override fun reset() {
            this.onChange = false
        }

        override val from: Int
            get() {
                checkState()
                return this.change.from
            }

        override val to: Int
            get() {
                checkState()
                return this.change.to
            }

        override val removed: List<E>
            get() {
                checkState()
                return this.change.removed ?: ArrayList()
            }

        override val permutation: IntArray
            get() {
                checkState()
                return this.change.perm
            }

        override val wasUpdated: Boolean
            get() {
                checkState()
                return this.change.updated
            }

        private fun checkState() {
            if (!this.onChange) {
                throw IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.")
            }
        }

        override fun toString(): String {
            val ret: String = if (this.change.perm.isNotEmpty()) {
                permChangeToString(this.change.perm)
            } else if (this.change.updated) {
                updateChangeToString(this.change.from, this.change.to)
            } else {
                addRemoveChangeToString(this.change.from, this.change.to, this.list, this.change.removed!!)
            }
            return "{ $ret }"
        }

    }

    private class IterableChange<E>(private val changes: Array<SubChange<E>>, list: ObservableList<E>) :
            Change<E>(list) {

        private var cursor = -1

        override operator fun next(): Boolean {
            if (this.cursor + 1 < this.changes.size) {
                ++cursor
                return true
            }
            return false
        }

        override fun reset() {
            this.cursor = -1
        }

        override val from: Int
            get() {
                checkState()
                return this.changes[cursor].from
            }

        override val to: Int
            get() {
                checkState()
                return this.changes[cursor].to
            }

        override val removed: List<E>
            get() {
                checkState()
                return this.changes[this.cursor].removed ?: ArrayList()
            }

        override val permutation: IntArray
            get() {
                checkState()
                return this.changes[this.cursor].perm
            }

        override val wasUpdated: Boolean
            get() {
                checkState()
                return this.changes[this.cursor].updated
            }

        private fun checkState() {
            check(this.cursor != -1) { "Invalid Change state: next() must be called before inspecting the Change." }
        }

        override fun toString(): String {
            var c = 0
            val b = StringBuilder()
            b.append("{ ")
            while (c < this.changes.size) {
                if (this.changes[c].perm.isNotEmpty()) {
                    b.append(permChangeToString(this.changes[c].perm))
                } else if (this.changes[c].updated) {
                    b.append(updateChangeToString(this.changes[c].from, this.changes[c].to))
                } else {
                    b.append(addRemoveChangeToString(this.changes[c].from, this.changes[c].to, this.list,
                            this.changes[c].removed!!))
                }
                if (c != this.changes.size - 1) {
                    b.append(", ")
                }
                ++c
            }
            b.append(" }")
            return b.toString()
        }

    }

}