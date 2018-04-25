package com.example.yuze.navigationdrawerdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignInRequest {

    @JsonProperty("user_name")
    private String userName;
    private String password;
}
