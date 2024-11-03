package com.group6.service;

        import com.group6.mapper.BillMapper;
        import com.group6.pojo.Bill;
        import com.group6.pojo.Result;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.math.BigDecimal;

@Service
public class BillService {

    @Autowired
    private BillMapper billMapper;

    public void addBill(Bill bill) {
        billMapper.insertBill(bill);
    }

    public void deleteBill(String id) {
        billMapper.deleteBillById(id);
    }

    public void updateBill(Bill bill) {
        billMapper.updateBill(bill);
    }

    public Bill getBillById(String id) {
        return billMapper.selectBillById(id);
    }

    public BigDecimal countByDormitory(int dormitory, int start_year, int start_month, int end_year, int end_month) {
        // 实现统计逻辑
        return new BigDecimal(0);
    }

    public BigDecimal countByBuilding(int building, int start_year, int start_month, int end_year, int end_month) {
        // 实现统计逻辑
        return new BigDecimal(0);
    }

    public BigDecimal countBySchool(int start_year, int start_month, int end_year, int end_month) {
        // 实现统计逻辑
        return new BigDecimal(0);
    }
}