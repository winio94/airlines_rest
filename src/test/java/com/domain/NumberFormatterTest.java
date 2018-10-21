package com.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberFormatterTest {

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWHenNegativeNumber() {
        new NumberFormatter().format(0);
    }
    
    @Test
    public void shouldThrowExceptionWhenNumberHigherThan3999() {
        new NumberFormatter().format(3999);
    }

    @Test
    public void shouldReturnProperResultForNumberLessThanTen() {
        assertEquals("I", new NumberFormatter().format(1));
        assertEquals("III", new NumberFormatter().format(3));
        assertEquals("IV", new NumberFormatter().format(4));
        assertEquals("V", new NumberFormatter().format(5));
        assertEquals("IX", new NumberFormatter().format(9));
    }

}