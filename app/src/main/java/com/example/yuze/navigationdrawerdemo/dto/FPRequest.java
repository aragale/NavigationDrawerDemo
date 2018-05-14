package com.example.yuze.navigationdrawerdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FPRequest {

    private String title;
    private String description;
    private List<String> images;
    @JsonProperty("trace_id")
    private String traceId;

}
