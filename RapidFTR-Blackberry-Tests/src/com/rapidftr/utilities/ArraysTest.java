package com.rapidftr.utilities;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class ArraysTest {

    @Test
    public void shouldNotBeEqualIfOneIsNullForByteArrays() {
        assertFalse(Arrays.equals(null, "hello".getBytes()));
        assertFalse(Arrays.equals("hello".getBytes(), null));
    }

    @Test
    public void shouldBeEqualForByteArrays() {
        assertTrue(Arrays.equals("hello".getBytes(),"hello".getBytes()));
    }

    @Test
    public void shouldNotBeEqualForByteArrays() {
        assertFalse(Arrays.equals("not".getBytes(),"hello".getBytes()));
    }

    @Test
    public void shouldNotBeEqualIfOneIsNullForObjectArrays() {
        assertFalse(Arrays.equals(null, new String[] {"hello", "boo"}));
        assertFalse(Arrays.equals(new String[] {"hello", "boo"}, null));
    }

    @Test
    public void shouldBeEqualForObjectArrays() {
        assertTrue(Arrays.equals(new String[] {"hello", "boo"},new String[] {"hello", "boo"}));
    }

    @Test
    public void shouldNotBeEqualForObjectArrays() {
        assertFalse(Arrays.equals(new String[] {"hello"}, new String[] {"hello", "boo"}));
    }

    @Test
    public void shouldReturnHashCodeForByteArrays() {
        assertTrue(Arrays.hashCode("hello".getBytes()) > 0);
    }

    @Test
    public void shouldReturnHashCodeForObjectArrays() {
        assertTrue(Arrays.hashCode(new String[] {"hello"}) > 0);
    }
}
