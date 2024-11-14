package com.group6.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

    private int id;
    private int year;
    private int month;
    private int days;
    private int building;
    private int dormitory;
    private double electricity_usage;
    private double electricity_cost;
    private double water_usage;
    private double water_cost;
    private double total_cost;

}
