package com.example.Windsurfer.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Integer cityID;
    private String state;
    private String city;
    private double temperature;
    private double windSpeed;

    public Location(Integer cityID,String city) {
        this.cityID = cityID;
        this.city = city;

    }
}
