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

package dev.lucasmdjl.fixedcapacityqueue

/**
 * A fixed capacity queue implementation using an ArrayList.
 *
 * Note: This implementation is not thread-safe. If multiple threads access
 * this queue concurrently, and at least one of the threads modifies the queue
 * structurally, it must be synchronized externally.
 *
 * @param capacity the maximum capacity of the queue. Must be positive.
 */
public class FixedCapacityArrayQueue<T : Any>(capacity: Int) : AbstractFixedCapacityArrayQueue<T>(capacity) {
    @Suppress("UNCHECKED_CAST")
    private val elements = arrayOfNulls<Any?>(capacity) as Array<T?>

    override fun getElement(i: Int): T = elements[i]!!

    override fun setElement(i: Int, element: T) {
        elements[i] = element
    }
}

/**
 * A fixed capacity queue implementation using a BooleanArray.
 *
 * Note: This implementation is not thread-safe. If multiple threads access
 * this queue concurrently, and at least one of the threads modifies the queue
 * structurally, it must be synchronized externally.
 *
 * @param capacity the maximum capacity of the queue. Must be positive.
 */
public class FixedCapacityBooleanArrayQueue(capacity: Int) : AbstractFixedCapacityArrayQueue<Boolean>(capacity) {
    private val elements = BooleanArray(capacity)

    override fun getElement(i: Int): Boolean = elements[i]

    override fun setElement(i: Int, element: Boolean) {
        elements[i] = element
    }
}

/**
 * A fixed capacity queue implementation using a ByteArray.
 *
 * Note: This implementation is not thread-safe. If multiple threads access
 * this queue concurrently, and at least one of the threads modifies the queue
 * structurally, it must be synchronized externally.
 *
 * @param capacity the maximum capacity of the queue. Must be positive.
 */
public class FixedCapacityByteArrayQueue(capacity: Int) : AbstractFixedCapacityArrayQueue<Byte>(capacity) {
    private val elements = ByteArray(capacity)

    override fun getElement(i: Int): Byte = elements[i]

    override fun setElement(i: Int, element: Byte) {
        elements[i] = element
    }
}

/**
 * A fixed capacity queue implementation using a CharArray.
 *
 * Note: This implementation is not thread-safe. If multiple threads access
 * this queue concurrently, and at least one of the threads modifies the queue
 * structurally, it must be synchronized externally.
 *
 * @param capacity the maximum capacity of the queue. Must be positive.
 */
public class FixedCapacityCharArrayQueue(capacity: Int) : AbstractFixedCapacityArrayQueue<Char>(capacity) {
    private val elements = CharArray(capacity)

    override fun getElement(i: Int): Char = elements[i]

    override fun setElement(i: Int, element: Char) {
        elements[i] = element
    }
}

/**
 * A fixed capacity queue implementation using an IntArray.
 *
 * Note: This implementation is not thread-safe. If multiple threads access
 * this queue concurrently, and at least one of the threads modifies the queue
 * structurally, it must be synchronized externally.
 *
 * @param capacity the maximum capacity of the queue. Must be positive.
 */
public class FixedCapacityIntArrayQueue(capacity: Int) : AbstractFixedCapacityArrayQueue<Int>(capacity) {
    private val elements = IntArray(capacity)

    override fun getElement(i: Int): Int = elements[i]

    override fun setElement(i: Int, element: Int) {
        elements[i] = element
    }
}

/**
 * A fixed capacity queue implementation using a LongArray.
 *
 * Note: This implementation is not thread-safe. If multiple threads access
 * this queue concurrently, and at least one of the threads modifies the queue
 * structurally, it must be synchronized externally.
 *
 * @param capacity the maximum capacity of the queue. Must be positive.
 */
public class FixedCapacityLongArrayQueue(capacity: Int) : AbstractFixedCapacityArrayQueue<Long>(capacity) {
    private val elements = LongArray(capacity)

    override fun getElement(i: Int): Long = elements[i]

    override fun setElement(i: Int, element: Long) {
        elements[i] = element
    }
}
