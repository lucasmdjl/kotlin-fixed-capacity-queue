/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.lucasmdjl.fixedcapacityqueue

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

public abstract class AbstractFixedCapacityArrayQueueTest<T : Any> {
    protected abstract fun getQueue(capacity: Int): AbstractFixedCapacityArrayQueue<T>
    protected abstract fun sampleElements(): Sequence<T>

    @Test
    public fun init_whenPositiveCapacity() {
        assertDoesNotThrow {
            getQueue(1)
        }
    }

    @Test
    public fun init_whenZeroCapacity() {
        assertThrows<IllegalArgumentException> {
            getQueue(0)
        }
    }

    @Test
    public fun init_whenNegativeCapacity() {
        assertThrows<IllegalArgumentException> {
            getQueue(-1)
        }
    }

    @Test
    public fun offer_whenNotFull() {
        val queue = getQueue(5)
        sampleElements().take(3).forEach {
            assertTrue(queue.offer(it))
        }
    }

    @Test
    public fun offer_whenFull() {
        val queue = getQueue(3)
        val sampleElements = sampleElements()
        sampleElements.take(3).forEach(queue::offer)
        val element = sampleElements.first()
        assertFalse(queue.offer(element))
    }

    @Test
    public fun poll_whenEmpty() {
        val queue = getQueue(3)
        assertNull(queue.poll())
    }

    @Test
    public fun poll_whenNotEmpty() {
        val queue = getQueue(5)
        sampleElements().take(5).forEach(queue::offer)
        repeat(3) {
            assertNotNull(queue.poll())
        }
    }

    @Test
    public fun pollIf_whenEmpty() {
        val queue = getQueue(3)
        assertNull(queue.pollIf { true })
    }

    @Test
    public fun pollIf_whenNotEmptyAndMatches() {
        val queue = getQueue(3)
        queue.offer(sampleElements().first())
        assertNotNull(queue.pollIf { true })
    }


    @Test
    public fun pollIf_whenNotEmptyAndNotMatches() {
        val queue = getQueue(3)
        queue.offer(sampleElements().first())
        assertNull(queue.pollIf { false })
    }

    @Test
    public fun peek_whenEmpty() {
        val queue = getQueue(3)
        assertNull(queue.peek())
    }

    @Test
    public fun peek_whenNotEmpty() {
        val queue = getQueue(5)
        sampleElements().take(3).forEach(queue::offer)
        val size1 = queue.size
        val peek1 = queue.peek()
        val size2 = queue.size
        val peek2 = queue.peek()
        assertEquals(peek1, peek2)
        assertEquals(size1, size2)
    }

    @Test
    public fun isFull_whenFull() {
        val queue = getQueue(5)
        sampleElements().take(5).forEach(queue::offer)
        assertTrue(queue.isFull())
    }

    @Test
    public fun isFull_whenNotFull() {
        val queue = getQueue(5)
        sampleElements().take(3).forEach(queue::offer)
        assertFalse(queue.isFull())
    }

    @Test
    public fun size_whenEmpty() {
        val queue = getQueue(5)
        assertEquals(0, queue.size)
    }

    @Test
    public fun size_whenFull() {
        val queue = getQueue(5)
        sampleElements().take(5).forEach(queue::offer)
        assertEquals(5, queue.size)
    }

    @Test
    public fun size_whenNotEmptyNorFull() {
        val queue = getQueue(5)
        sampleElements().take(3).forEach(queue::offer)
        assertEquals(3, queue.size)
    }

    @Test
    public fun size_afterOffersAndPolls() {
        val queue = getQueue(5)
        sampleElements().take(5).forEach(queue::offer)
        queue.poll()
        queue.poll()
        assertEquals(3, queue.size)
    }

    @Test
    public fun poll_afterOffers_inFIFO() {
        val queue = getQueue(3)
        val elements = sampleElements().take(3).toList()
        elements.forEach(queue::offer)
        val returnedElements = ArrayList<T>()
        repeat(3) {
            returnedElements.add(queue.poll()!!)
        }
        assertEquals(elements, returnedElements)
    }

    @Test
    public fun iterator_inFIFOAndNotConsumes() {
        val queue = getQueue(3)
        val elements = sampleElements().take(3).toList()
        elements.forEach(queue::offer)
        val returnedElements = ArrayList<T>()
        val iterator = queue.iterator()
        while (iterator.hasNext()) {
            returnedElements.add(iterator.next())
        }
        assertEquals(elements, returnedElements)
        assertEquals(3, queue.size)
    }

    @Test
    public fun consumingIterator_inFIFOAndConsumes() {
        val queue = getQueue(3)
        val elements = sampleElements().take(3).toList()
        elements.forEach(queue::offer)
        val returnedElements = ArrayList<T>()
        val iterator = queue.headRemovingIterator()
        while (iterator.hasNext()) {
            returnedElements.add(iterator.next())
            iterator.remove()
        }
        assertEquals(elements, returnedElements)
        assertEquals(0, queue.size)
    }

    @Test
    public fun offer_afterPoll() {
        val queue = getQueue(3)
        sampleElements().take(3).toList().forEach(queue::offer)
        queue.poll()
        assertTrue(queue.offer(sampleElements().first()))
    }

    @Test
    public fun poll_afterPeek() {
        val queue = getQueue(3)
        val element = sampleElements().first()
        queue.offer(element)
        val peekedElement = queue.peek()
        val polledElement = queue.poll()
        assertEquals(peekedElement, polledElement)
    }

    @Test
    public fun offer_afterQueueBecomesEmpty() {
        val queue = getQueue(3)
        sampleElements().take(3).toList().forEach(queue::offer)
        repeat(3) { queue.poll() }
        val newElement = sampleElements().first()
        assertTrue(queue.offer(newElement))
        assertEquals(newElement, queue.peek())
    }
}
