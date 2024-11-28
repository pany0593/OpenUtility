package com.group6.service;

import com.group6.mapper.BillMapper;
import com.group6.pojo.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class BillService {

    @Autowired
    private BillMapper billMapper;


    public void addBill(Bill bill) {
        UUID uuid = UUID.randomUUID();
        long uniqueId = uuid.getMostSignificantBits() & Long.MAX_VALUE;
        String billId=""+uniqueId;
        bill.setId(billId);
    }
    public void deleteBill(String id) throws Exception {

        try {
            if(billMapper.deleteBill(id) == 0)
            {
                throw new Exception("删除错误");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBill(Bill bill) {
        billMapper.updateBill(bill);
    }

    public Bill getBill(Bill bill) {
        return billMapper.selectBillById(bill);
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
}