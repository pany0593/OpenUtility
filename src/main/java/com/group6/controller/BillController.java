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

    @PostMapping("/AddBill")
    public Result AddBill(@RequestBody Bill bill) {
        if(bill == null) {
            return Result.error("账单为空");
        }
        try {
            billService.addBill(bill);
            return Result.success("账单添加成功");
        } catch (Exception e) {
            return Result.error("账单添加失败");
        }
    }

    @PostMapping("/deleteBill")
    public Result deleteBill(@RequestBody String id) {
        if(id == null) {
            return Result.error("账单ID为空");
        }
        try {
            billService.deleteBill(id);
            return Result.success("账单删除成功");
        } catch (Exception e) {
            return Result.error("账单删除失败");
        }
    }

    @PostMapping("/uploadBill")
    public Result uploadBill(@RequestBody Bill bill) {
        if(bill == null || bill.getId() == 0) {
            return Result.error("账单信息不完整");
        }
        try {
            billService.updateBill(bill);
            return Result.success("账单更新成功");
        } catch (Exception e) {
            return Result.error("账单更新失败");
        }
    }

    @GetMapping("/getData")
    public Result getData(String id) {
        if(id == null) {
            return Result.error("账单ID为空");
        }
        try {
            Bill bill = billService.getBillById(id);
            if(bill == null) {
                return Result.error("账单不存在");
            }
            return Result.success(bill);
        } catch (Exception e) {
            return Result.error("账单查询失败");
        }
    }

    @GetMapping("/count_dormitory")
    public Result countDormitory(int dormitory, int start_year, int start_month, int end_year, int end_month) {
        try {
            BigDecimal total = billService.countByDormitory(dormitory, start_year, start_month, end_year, end_month);
            return Result.success(total);
        } catch (Exception e) {
            return Result.error("宿舍水电费统计失败");
        }
    }

    @GetMapping("/count_building")
    public Result countBuilding(int building, int start_year, int start_month, int end_year, int end_month) {
        try {
            BigDecimal total = billService.countByBuilding(building, start_year, start_month, end_year, end_month);
            return Result.success(total);
        } catch (Exception e) {
            return Result.error("楼栋水电费统计失败");
        }
    }

    @GetMapping("/count_school")
    public Result countSchool(int start_year, int start_month, int end_year, int end_month) {
        try {
            BigDecimal total = billService.countBySchool(start_year, start_month, end_year, end_month);
            return Result.success(total);
        } catch (Exception e) {
            return Result.error("全校水电费统计失败");
        }
    }

}
