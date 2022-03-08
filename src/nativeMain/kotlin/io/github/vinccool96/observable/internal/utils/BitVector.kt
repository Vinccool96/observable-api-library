package io.github.vinccool96.observable.internal.utils

actual class BitVector actual constructor(size: Int) {

    private var bits: LongArray = LongArray(bitToElementSize(size))

    private val lastIndex: Int
        get() = size - 1

    /** Actual number of bits available in the set. All bits with indices >= size assumed to be 0 */
    private var size: Int = size

    /** True if this BitSet contains no bits set to true. */
    val isEmpty: Boolean
        get() = bits.all { it == ALL_FALSE }

    // Transforms a bit index into an element index in the `bits` array.
    private val Int.elementIndex: Int
        get() = this / ELEMENT_SIZE

    // Transforms a bit index in the set into a bit in the element of the `bits` array.
    private val Int.bitOffset: Int
        get() = this % ELEMENT_SIZE

    // Transforms a bit index in the set into pair of a `bits` element index and a bit index in the element.
    private val Int.asBitCoordinates: Pair<Int, Int>
        get() = Pair(elementIndex, bitOffset)

    // Transforms a bit offset to the mask with only bit set corresponding to the offset.
    private val Int.asMask: Long
        get() = 0x1L shl this

    // Transforms a bit offset to the mask with only bits before the index (inclusive) set.
    private val Int.asMaskBefore: Long
        get() = getMaskBetween(0, this)

    // Builds a masks with 1 between fromOffset and toOffset (both inclusive).
    private fun getMaskBetween(fromOffset: Int, toOffset: Int): Long {
        var res = 0L
        val maskToAdd = fromOffset.asMask
        for (i in fromOffset..toOffset) {
            res = (res shl 1) or maskToAdd
        }
        return res
    }

    // Transforms a size in bits to a size in elements of the `bits` array.
    private fun bitToElementSize(bitSize: Int): Int = (bitSize + ELEMENT_SIZE - 1) / ELEMENT_SIZE

    // Transforms a pair of an element index and a bit offset to a bit index.
    private fun bitIndex(elementIndex: Int, bitOffset: Int) =
            elementIndex * ELEMENT_SIZE + bitOffset

    // Sets all bits after the last available bit (size - 1) to 0.
    private fun clearUnusedTail() {
        val (lastElementIndex, lastBitOffset) = lastIndex.asBitCoordinates
        bits[bits.lastIndex] = bits[bits.lastIndex] and lastBitOffset.asMaskBefore
        for (i in lastElementIndex + 1 until bits.size) {
            bits[i] = ALL_FALSE
        }
    }

    // Internal function. Sets bits specified by the element index and the given mask to value.
    private fun setBitsWithMask(elementIndex: Int, mask: Long, value: Boolean) {
        val element = bits[elementIndex]
        if (value) {
            bits[elementIndex] = element or mask
        } else {
            bits[elementIndex] = element and mask.inv()
        }
    }

    /**
     * Checks if index is valid and extends the `bits` array if the index exceeds its size.
     * @throws [IndexOutOfBoundsException] if [index] < 0.
     */
    private fun ensureCapacity(index: Int) {
        if (index < 0) {
            throw IndexOutOfBoundsException()
        }
        if (index >= size) {
            size = index + 1
            if (index.elementIndex >= bits.size) {
                // Create a new array containing the index-th bit.
                bits = bits.copyOf(bitToElementSize(index + 1))
            }
            // Set all bits after the index to 0.
            clearUnusedTail()
        }
    }

    actual fun set(index: Int, value: Boolean) {
        if (index < 0) {
            throw IndexOutOfBoundsException("bitIndex < 0: $index")
        }

        ensureCapacity(index)
        val (elementIndex, offset) = index.asBitCoordinates
        setBitsWithMask(elementIndex, offset.asMask, value)
    }

    /**
     * Returns the biggest index of a bit which value is [lookFor] before [startIndex] (inclusive).
     * Returns -1 if there is no such bits before [startIndex].
     * If [startIndex] >= [size] returns -1
     */
    private fun previousBit(startIndex: Int, lookFor: Boolean): Int {
        var correctStartIndex = startIndex
        if (startIndex >= size) {
            // We assume that all bits after `size - 1` are 0. So we can return the start index if we are looking for 0.
            if (!lookFor) {
                return startIndex
            } else {
                // If we are looking for 1 we can skip all these 0 after `size - 1`.
                correctStartIndex = size - 1
            }
        }
        if (correctStartIndex < -1) {
            throw IndexOutOfBoundsException()
        }
        if (correctStartIndex == -1) {
            return -1
        }

        val (startElementIndex, startOffset) = correctStartIndex.asBitCoordinates
        // Look for the next set bit in the first element.
        var element = bits[startElementIndex]
        for (offset in startOffset downTo 0) {
            val bit = element and (0x1L shl offset) != 0L
            if (bit == lookFor) {  // Look for not 0 if we need a set bit and look for 0 otherwise.
                return bitIndex(startElementIndex, offset)
            }
        }
        // Look for in the remaining elements.
        for (index in startElementIndex - 1 downTo 0) {
            element = bits[index]
            for (offset in MAX_BIT_OFFSET downTo 0) {
                val bit = element and (0x1L shl offset) != 0L
                if (bit == lookFor) {  // Look for not 0 if we need a set bit and look for 0 otherwise.
                    return bitIndex(index, offset)
                }
            }
        }
        return -1
    }

    /**
     * Returns the biggest index of a bit which value is `true` before [startIndex] (inclusive).
     * Returns -1 if there is no such bits before [startIndex] or if [startIndex] == -1.
     * If [startIndex] >= size will search from (size - 1)-th bit.
     */
    actual fun previousSetBit(startIndex: Int): Int = previousBit(startIndex, true)

    actual operator fun get(index: Int): Boolean {
        if (index < 0) {
            throw IndexOutOfBoundsException()
        }
        if (index >= size) {
            return false
        }
        val (elementIndex, offset) = index.asBitCoordinates
        return bits[elementIndex] and offset.asMask != 0L
    }

    companion object {

        private const val ELEMENT_SIZE = 64

        private const val MAX_BIT_OFFSET = ELEMENT_SIZE - 1

        private const val ALL_FALSE = 0L // 0x0000_0000_0000_0000

    }

}

actual val BitVector.empty: Boolean
    get() = this.isEmpty