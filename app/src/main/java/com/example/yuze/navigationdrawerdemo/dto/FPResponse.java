package com.example.yuze.navigationdrawerdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class FPResponse {

    private String id;
    private String title;
    private String description;
    private List<String> images;
    @JsonProperty("trace_id")
    private String traceId;
    private String time;
}
