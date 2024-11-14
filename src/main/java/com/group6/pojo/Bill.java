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
    private BigDecimal electricity_usage;
    private BigDecimal electricity_cost;
    private BigDecimal water_usage;
    private BigDecimal water_cost;
    private BigDecimal total_cost;

}
