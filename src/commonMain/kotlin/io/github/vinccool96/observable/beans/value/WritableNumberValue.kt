package io.github.vinccool96.observable.beans.value

/**
 * A tagging interface that marks all sub-interfaces of [WritableValue] that wrap a number.
 *
 * @see WritableValue
 * @see WritableDoubleValue
 * @see WritableFloatValue
 * @see WritableIntValue
 * @see WritableLongValue
 * @see WritableShortValue
 * @see WritableByteValue
 */
interface WritableNumberValue : WritableValue<Number?>