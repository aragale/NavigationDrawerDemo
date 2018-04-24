package com.example.yuze.navigationdrawerdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SignUpRequest {

    @JsonProperty("user_name")
    private String userName;

    private String password;
}
