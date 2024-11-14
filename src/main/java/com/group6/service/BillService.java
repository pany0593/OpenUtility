package com.group6.service;

import com.group6.mapper.BillMapper;
import com.group6.pojo.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BillService {

    @Autowired
    private BillMapper billMapper;


    public boolean addBill(Bill bill) {
        return billMapper.insertBill(bill) > 0;
    }
    public boolean deleteBill(String id) {
        return billMapper.deleteBill(id) > 0;
    }

    public boolean updateBill(Bill bill) {
        return billMapper.updateBill(bill) > 0;
    }

    public Bill getBill(String id) {
        return billMapper.selectBillById(id);
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