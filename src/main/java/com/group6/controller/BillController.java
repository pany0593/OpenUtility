package com.group6.controller;


import com.group6.pojo.Bill;
import com.group6.pojo.Result;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bill")
public class BillController {
    @PostMapping("/Add")
    public Result Add(Bill bill) {

    }

    @PostMapping("/delete")
    public Result delete(String id) {

    }

    @PostMapping("/upload")
    public Result upload(Bill bill) {

    }

    @GetMapping("/getData")
    public Result getData(String id) {

    }

    @GetMapping("/count_dormitory")
    public Result count_dormitory(int id, int start_year, int start_month, int end_year, int end_month) {

    }

    @GetMapping("/count_building")
    public Result count_building(int building, int start_year, int start_month, int end_year, int end_month) {

    }

    @GetMapping("/count_school")
    public Result count_school(int start_year, int start_month, int end_year, int end_month) {

    }

}
