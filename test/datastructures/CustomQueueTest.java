package datastructures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CustomQueue&lt;T&gt; — singly-linked FIFO queue.
 * Covers: empty state, enqueue/dequeue FIFO ordering, dequeue-when-empty,
 * mixed enqueue/dequeue sequences.
 *
 * @author Sun Chengyang
 */
public class CustomQueueTest {

    private CustomQueue<String> queue;

    @Before
    public void setUp() {
        queue = new CustomQueue<>();
    }

    @Test
    public void testEmpty_isEmptyTrue() {
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEmpty_dequeueReturnsNull() {
        assertNull(queue.dequeue());
    }

    @Test
    public void testEnqueue_isEmptyFalse() {
        queue.enqueue("a");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueueDequeue_returnsFirstEnqueued() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        assertEquals("a", queue.dequeue());
    }

    @Test
    public void testMultipleDequeues_fifoOrder() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        assertEquals("a", queue.dequeue());
        assertEquals("b", queue.dequeue());
        assertEquals("c", queue.dequeue());
    }

    @Test
    public void testDequeueAll_emptyAgain() {
        queue.enqueue("a");
        queue.enqueue("b");
        queue.dequeue();
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMixedEnqueueDequeue_correctOrder() {
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals("a", queue.dequeue());
        queue.enqueue("c");
        assertEquals("b", queue.dequeue());
        assertEquals("c", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
}
