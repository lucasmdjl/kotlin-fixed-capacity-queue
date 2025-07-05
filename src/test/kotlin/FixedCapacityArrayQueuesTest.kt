/*
 * Fixed Capacity Queue: a Kotlin micro-library with fixed capacity queue implementations.
 * Copyright (C) 2025 Lucas M. de Jong Larrarte
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.lucasmdjl.fixedqueuecapacity

private class InfiniteIterator<T>(initial: T, private val computeNext: (T) -> T): Iterator<T> {
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
