package com.example.yuze.navigationdrawerdemo;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

}