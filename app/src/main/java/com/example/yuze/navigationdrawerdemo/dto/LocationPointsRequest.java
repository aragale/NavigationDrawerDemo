package com.example.yuze.navigationdrawerdemo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LocationPointsRequest {

    private String time;
    private String longitude;
    private String latitude;
}
