package datastructures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.EmptyStackException;

/**
 * Tests for CustomStack&lt;T&gt; — singly-linked LIFO stack.
 * Covers: empty state, push/pop, isEmpty, LIFO ordering,
 * EmptyStackException on pop of empty stack.
 *
 * @author Sun Chengyang
 */
public class CustomStackTest {

    private CustomStack<String> stack;

    @Before
    public void setUp() {
        stack = new CustomStack<>();
    }

    @Test
    public void testEmpty_isEmptyTrue() {
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testPushOne_isEmptyFalse() {
        stack.push("a");
        assertFalse(stack.isEmpty());
    }

    @Test
    public void testPushPop_returnsLastPushed() {
        stack.push("a");
        stack.push("b");
        stack.push("c");
        assertEquals("c", stack.pop());
    }

    @Test
    public void testMultiplePops_lifoOrder() {
        stack.push("a");
        stack.push("b");
        stack.push("c");
        assertEquals("c", stack.pop());
        assertEquals("b", stack.pop());
        assertEquals("a", stack.pop());
    }

    @Test
    public void testPopAll_emptyAgain() {
        stack.push("a");
        stack.push("b");
        stack.pop();
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test(expected = EmptyStackException.class)
    public void testPop_onEmptyStack_throwsException() {
        stack.pop();  // expected to throw
    }

    @Test(expected = EmptyStackException.class)
    public void testPop_afterAllPopped_throwsException() {
        stack.push("a");
        stack.pop();
        // Now empty again — another pop must throw.
        stack.pop();
    }
}
