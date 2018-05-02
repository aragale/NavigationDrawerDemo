package com.example.yuze.navigationdrawerdemo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationPointsResponse {

    private String id;

    private List<LocationPoint> positions;
}
