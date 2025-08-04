/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.lucasmdjl.fixedcapacityqueue

private class InfiniteIterator<T>(initial: T, private val computeNext: (T) -> T) : Iterator<T> {
    private var current: T = initial

    override fun hasNext(): Boolean = true

    override fun next(): T {
        val result = current
        current = computeNext(current)
        return result
    }

}

public data class TestData(
    val int: Int = 42,
    val string: String = ""
)

public class FixedCapacityArrayQueueTest : AbstractFixedCapacityArrayQueueTest<TestData>() {
    override fun getQueue(capacity: Int): AbstractFixedCapacityArrayQueue<TestData> = FixedCapacityArrayQueue(capacity)

    override fun sampleElements(): Sequence<TestData> = Sequence {
        InfiniteIterator(TestData()) {
            TestData(it.int + 1, it.string + "1")
        }
    }
}

public class FixedCapacityLongArrayQueueTest : AbstractFixedCapacityArrayQueueTest<Long>() {

    override fun getQueue(capacity: Int): FixedCapacityLongArrayQueue = FixedCapacityLongArrayQueue(capacity)

    override fun sampleElements(): Sequence<Long> = Sequence { InfiniteIterator(42) { it + 1 } }
}

public class FixedCapacityIntArrayQueueTest : AbstractFixedCapacityArrayQueueTest<Int>() {

    override fun getQueue(capacity: Int): FixedCapacityIntArrayQueue = FixedCapacityIntArrayQueue(capacity)

    override fun sampleElements(): Sequence<Int> = Sequence { InfiniteIterator(42) { it + 1 } }
}

public class FixedCapacityByteArrayQueueTest : AbstractFixedCapacityArrayQueueTest<Byte>() {

    override fun getQueue(capacity: Int): FixedCapacityByteArrayQueue = FixedCapacityByteArrayQueue(capacity)

    override fun sampleElements(): Sequence<Byte> = Sequence { InfiniteIterator(42) { (it + 1).toByte() } }
}

public class FixedCapacityBooleanArrayQueueTest : AbstractFixedCapacityArrayQueueTest<Boolean>() {

    override fun getQueue(capacity: Int): FixedCapacityBooleanArrayQueue = FixedCapacityBooleanArrayQueue(capacity)

    override fun sampleElements(): Sequence<Boolean> = Sequence { InfiniteIterator(true) { !it } }
}

public class FixedCapacityCharArrayQueueTest : AbstractFixedCapacityArrayQueueTest<Char>() {

    override fun getQueue(capacity: Int): FixedCapacityCharArrayQueue = FixedCapacityCharArrayQueue(capacity)

    override fun sampleElements(): Sequence<Char> = Sequence { InfiniteIterator('A') { it + 1 } }
}

public class FixedCapacityFloatArrayQueueTest : AbstractFixedCapacityArrayQueueTest<Float>() {

    override fun getQueue(capacity: Int): FixedCapacityFloatArrayQueue = FixedCapacityFloatArrayQueue(capacity)

    override fun sampleElements(): Sequence<Float> = Sequence { InfiniteIterator(3.14f) { it + 0.42f } }
}

public class FixedCapacityDoubleArrayQueueTest : AbstractFixedCapacityArrayQueueTest<Double>() {

    override fun getQueue(capacity: Int): FixedCapacityDoubleArrayQueue = FixedCapacityDoubleArrayQueue(capacity)

    override fun sampleElements(): Sequence<Double> = Sequence { InfiniteIterator(3.14) { it + 0.42 } }
}

public class FixedCapacityShortArrayQueueTest : AbstractFixedCapacityArrayQueueTest<Short>() {

    override fun getQueue(capacity: Int): FixedCapacityShortArrayQueue = FixedCapacityShortArrayQueue(capacity)

    override fun sampleElements(): Sequence<Short> = Sequence { InfiniteIterator(42) { (it + 1).toShort() } }
}
