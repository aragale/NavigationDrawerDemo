package com.example.yuze.navigationdrawerdemo.dto;

import android.media.Image;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class FPResponse {

    @JsonProperty("id")
    private String FPId;
    private String title;
    private String description;
    private List<Image> images;
    @JsonProperty("trace_id")
    private String traceId;
}
