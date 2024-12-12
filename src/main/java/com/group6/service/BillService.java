package com.group6.service;

import com.group6.mapper.BillMapper;
import com.group6.pojo.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BillService {

    @Autowired
    private BillMapper billMapper;


    public void addBill(Bill bill) {
        UUID uuid = UUID.randomUUID();
        long uniqueId = uuid.getMostSignificantBits() & Long.MAX_VALUE;
        String billId=""+uniqueId;
        billId="bill"+billId;
        bill.setId(billId);
        try {
            if(billMapper.findBillById(bill.getId()) != null) {
                throw new Exception("编号重复");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        billMapper.insertBill(bill);
    }
    public void deleteBill(Bill bill) {

        try {
            if(billMapper.findBillById(bill.getId()) == null) {
                throw new Exception("删除错误,记录不存在");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        billMapper.deleteBill(bill);
    }

    public void updateBill(Bill bill) {

        try {
            if(billMapper.findBillById(bill.getId()) == null) {
                throw new Exception("没有这条记录");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        billMapper.updateBill(bill);
    }

    public Bill getBill(Bill bill) {
        try {
            if(billMapper.findBillById(bill.getId()) == null) {
                throw new Exception("没有这条记录");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return billMapper.selectBillById(bill);
    }

    public Bill getBillByDormitory(Bill bill) {
        try {
            if(billMapper.findBillByDormitory(bill) == null) {
                throw new Exception("没有这条记录");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return billMapper.findBillByDormitory(bill);
    }
    public List<Bill> getAllBill() {
        return billMapper.selectAllBill();
    }

    public BigDecimal countByDormitory(int dormitory, int startYear, int startMonth, int endYear, int endMonth) {
        return billMapper.sumElectricityAndWaterCostByDormitory(dormitory, startYear, startMonth, endYear, endMonth);
    }

    public BigDecimal countByBuilding(int building, int startYear, int startMonth, int endYear, int endMonth) {
        return billMapper.sumElectricityAndWaterCostByBuilding(building, startYear, startMonth, endYear, endMonth);
    }

    public BigDecimal countBySchool(int startYear, int startMonth, int endYear, int endMonth) {
        return billMapper.sumElectricityAndWaterCostBySchool(startYear, startMonth, endYear, endMonth);
    }

    /**
     * 提交账单申诉
     */
    public void submitAppeal(String billId, String userId, String reason) {
        billMapper.insertAppeal(billId, userId, reason);
    }

    /**
     * 查询所有账单申诉记录
     */
    public List<Map<String, Object>> listAppeals(String status) {
        if (status == null || status.isEmpty()) {
            // 查询所有申诉记录
            return billMapper.listAllAppeals();
        } else {
            // 查询指定状态的申诉记录
            return billMapper.listAppealsByStatus(status);
        }
    }

    /**
     * 同意账单申诉并更新账单
     */
    public void approveAppeal(String appealId, Bill updatedBill) {
        if (updatedBill == null) {
            throw new IllegalArgumentException("Updated bill data must be provided.");
        }
        // 更新账单信息
        billMapper.updateBill(updatedBill);
        
        // 更新申诉状态为已批准
        billMapper.approveAppeal(appealId);
    }

    /**
     * 拒绝账单申诉
     */
    public void rejectAppeal(String appealId, String rejectReason) {
        if (rejectReason == null || rejectReason.isEmpty()) {
            throw new IllegalArgumentException("Reject reason must be provided.");
        }
        // 更新申诉状态为已拒绝并记录拒绝理由
        billMapper.rejectAppeal(appealId, rejectReason);
    }
}