package com.group6.controller;

import com.group6.pojo.Bill;
import com.group6.pojo.Result;
import com.group6.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;


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
    public Result delete(@RequestBody Bill bill) {
        try {
            billService.deleteBill(bill);
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

    @PostMapping("/getData")
    public Result<Bill> getData(@RequestBody Bill bbill) {
        try {
            Bill bill = billService.getBill(bbill);
            return Result.success(bill);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/getDataByDormitory")
    public Result<Bill> getDataByDormitory(@RequestBody Bill bbill) {
        try {
            Bill bill = billService.getBillByDormitory(bbill);
            return Result.success(bill);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    @PostMapping("/getAllData")
    public Result<List<Bill>> getAllData() {
        try {
            List<Bill> bills = billService.getAllBill();
            return Result.success(bills);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/rangeByMonth")
    public Result<List<Bill>> rangeByMonth(@RequestBody Bill bill) {
        try {
            List<Bill> bills = billService.rangeByMonth(bill);
            return Result.success(bills);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}