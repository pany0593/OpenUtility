package com.group6.controller;

import com.group6.pojo.Bill;
import com.group6.pojo.Result;
import com.group6.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/add")
    public Result add(@RequestBody Bill bill) {
        if (billService.addBill(bill)) {
            return Result.success();
        } else {
            return Result.error("Failed to add bill");
        }
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam String id) {
        if (billService.deleteBill(id)) {
            return Result.success();
        } else {
            return Result.error("Failed to delete bill");
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody Bill bill) {
        if (billService.updateBill(bill)) {
            return Result.success();
        } else {
            return Result.error("Failed to update bill");
        }
    }

    @GetMapping("/getData")
    public Result<Bill> getData(@RequestParam String id) {
        Bill bill = billService.getBill(id);
        return bill != null ? Result.success(bill) : Result.error("Bill not found");
    }

    @GetMapping("/count_dormitory")
    public Result<BigDecimal> countDormitory(
            @RequestParam int dormitory, @RequestParam int startYear,
            @RequestParam int startMonth, @RequestParam int endYear,
            @RequestParam int endMonth) {
        return Result.success(billService.countByDormitory(dormitory, startYear, startMonth, endYear, endMonth));
    }

    @GetMapping("/count_building")
    public Result<BigDecimal> countBuilding(
            @RequestParam int building, @RequestParam int startYear,
            @RequestParam int startMonth, @RequestParam int endYear,
            @RequestParam int endMonth) {
        return Result.success(billService.countByBuilding(building, startYear, startMonth, endYear, endMonth));
    }

    @GetMapping("/count_school")
    public Result<BigDecimal> countSchool(
            @RequestParam int startYear, @RequestParam int startMonth,
            @RequestParam int endYear, @RequestParam int endMonth) {
        return Result.success(billService.countBySchool(startYear, startMonth, endYear, endMonth));
    }
}