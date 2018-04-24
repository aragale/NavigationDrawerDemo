package com.example.yuze.navigationdrawerdemo;

import com.example.yuze.navigationdrawerdemo.dto.SignUpRequest;
import com.example.yuze.navigationdrawerdemo.dto.SignUpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import static org.junit.Assert.*;

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

    @Test
    public void signUp() {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
                .userName("hello")
                .password("123456")
                .build();
        try {
            final String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);
            final String signUpResponseJson = HttpUtils.post(
                    Constants.HOST + Constants.USERS,
                    signUpRequestJson);
            final SignUpResponse signUpResponse = objectMapper.readValue(signUpResponseJson, SignUpResponse.class);
            System.out.println(signUpResponse);
        } catch (Exception e) {
            e.printStackTrace();
            //System.err.println("Exception: asas");
        }
    }
}