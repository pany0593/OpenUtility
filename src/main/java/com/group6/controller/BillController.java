package com.group6.controller;

import com.group6.pojo.Bill;
import com.group6.pojo.Result;
import com.group6.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


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

    /**
     * 提交账单申诉
     */
    @PostMapping("/appeal/submit")
    public Result submitAppeal(
            @RequestParam String billId,
            @RequestParam String userId,
            @RequestParam String reason) {
        try {
            billService.submitAppeal(billId, userId, reason);
            return Result.success("Appeal submitted successfully.");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询账单申诉记录
     */
    @GetMapping("/appeal/list")
    public Result<List<Map<String, Object>>> listAppeals(@RequestParam(required = false) String status) {
        try {
            List<Map<String, Object>> appeals = billService.listAppeals(status);
            return Result.success(appeals);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 管理员处理账单申诉
     */
    @PostMapping("/appeal/resolve")
    public Result resolveAppeal(
            @RequestParam String appealId,
            @RequestParam boolean isApproved,
            @RequestBody(required = false) Bill updatedBill,
            @RequestParam(required = false) String rejectReason) {
        try {
            if (isApproved) {
                billService.approveAppeal(appealId, updatedBill);
                return Result.success("Appeal approved and bill updated successfully.");
            } else {
                billService.rejectAppeal(appealId, rejectReason);
                return Result.success("Appeal rejected with reason: " + rejectReason);
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}