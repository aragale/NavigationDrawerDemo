package com.example.yuze.navigationdrawerdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponse {

    @JsonProperty("user_id")
    private String userId;
    private String session;
}
