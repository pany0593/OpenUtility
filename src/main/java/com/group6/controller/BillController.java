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
        try {
            billService.addBill(bill);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    @PostMapping("/delete")
    public Result delete(@RequestBody String id) {
        try {
            billService.deleteBill(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody Bill bill) {
        try {
            billService.updateBill(bill);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }

    @GetMapping("/getData")
    public Result<Bill> getData(@RequestBody Bill bbill) {
        try {
            Bill bill = billService.getBill(bbill);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
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