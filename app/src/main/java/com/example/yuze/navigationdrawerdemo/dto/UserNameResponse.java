package com.example.yuze.navigationdrawerdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserNameResponse {

    @JsonProperty("id")
    private String userId;
    @JsonProperty("name")
    private String userName;
}
