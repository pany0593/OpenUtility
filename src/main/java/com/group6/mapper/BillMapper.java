package com.group6.mapper;

import com.group6.pojo.Bill;
import com.group6.pojo.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface BillMapper {

    @Select("SELECT * FROM water_electricity_bill WHERE id = #{id}")
    Bill findBillById(@Param("id") String id);

    @Select("SELECT * FROM water_electricity_bill WHERE building = #{bill.building} AND dormitory = #{bill.dormitory} AND year = #{bill.year} AND month = #{bill.month}")
    Bill findBillByDormitory(@Param("bill") Bill bill);

    @Insert("INSERT INTO water_electricity_bill (id,year,month,days,building,dormitory,electricity_usage,electricity_cost,water_usage,water_cost,total_cost) " +
            "VALUES (#{bill.id}, #{bill.year}, #{bill.month}, #{bill.days}, #{bill.building}, #{bill.dormitory}, #{bill.electricity_usage}, #{bill.electricity_cost}, #{bill.water_usage}, #{bill.water_cost}, #{bill.total_cost})")
    int insertBill(@Param("bill") Bill bill);

    @Delete("DELETE FROM water_electricity_bill WHERE id = #{bill.id}")
    int deleteBill(@Param("bill") Bill bill);

    @Update("UPDATE water_electricity_bill SET year = #{bill.year}, month = #{bill.month}, days = #{bill.days}, building = #{bill.building}, dormitory = #{bill.dormitory}, " +
            "electricity_usage = #{bill.electricity_usage}, electricity_cost = #{bill.electricity_cost}, water_usage = #{bill.water_usage}, water_cost = #{bill.water_cost}, total_cost = #{bill.total_cost} WHERE id = #{bill.id}")
    int updateBill(@Param("bill") Bill bill);

    @Select("SELECT * FROM water_electricity_bill WHERE id = #{bill.id}")
    Bill selectBillById(@Param("bill") Bill bill);

    @Select("SELECT * FROM water_electricity_bill")
    List<Bill> selectAllBill();
    @Select("SELECT * FROM water_electricity_bill WHERE year = #{bill.year} AND month = #{bill.month} ORDER BY total_cost DESC")
    List<Bill> rangeByMonth(@Param("bill") Bill bill);


    @Select("SELECT SUM(electricity_cost + water_cost) FROM bill WHERE dormitory = #{dormitory} AND " +
            "(year * 12 + month) BETWEEN (#{startYear} * 12 + #{startMonth}) AND (#{endYear} * 12 + #{endMonth})")
    BigDecimal sumElectricityAndWaterCostByDormitory(@Param("dormitory") int dormitory,
                                                     @Param("startYear") int startYear, @Param("startMonth") int startMonth,
                                                     @Param("endYear") int endYear, @Param("endMonth") int endMonth);

    @Select("SELECT SUM(electricity_cost + water_cost) FROM bill WHERE building = #{building} AND " +
            "(year * 12 + month) BETWEEN (#{startYear} * 12 + #{startMonth}) AND (#{endYear} * 12 + #{endMonth})")
    BigDecimal sumElectricityAndWaterCostByBuilding(@Param("building") int building,
                                                    @Param("startYear") int startYear, @Param("startMonth") int startMonth,
                                                    @Param("endYear") int endYear, @Param("endMonth") int endMonth);

    @Select("SELECT SUM(electricity_cost + water_cost) FROM bill WHERE " +
            "(year * 12 + month) BETWEEN (#{startYear} * 12 + #{startMonth}) AND (#{endYear} * 12 + #{endMonth})")
    BigDecimal sumElectricityAndWaterCostBySchool(@Param("startYear") int startYear, @Param("startMonth") int startMonth,
                                                  @Param("endYear") int endYear, @Param("endMonth") int endMonth);

    /**
     * 插入账单申诉记录
     */
    @Insert("INSERT INTO bill_appeals (id, bill_id, user_id, reason, status, created_time) " +
            "VALUES (SUBSTRING(UUID(), 1, 30), #{billId}, #{userId}, #{reason}, 'PENDING', NOW())")
    void insertAppeal(@Param("billId") String billId, @Param("userId") String userId, @Param("reason") String reason);

    /**
     * 查询所有账单申诉记录
     */
    @Select("SELECT a.id AS appeal_id, " +
            "u.username AS user_name, " +
            "u.building, " +
            "u.dormitory, " +
            "b.year, " +
            "b.month, " +
            "b.water_cost, " +
            "b.electricity_cost, " +
            "a.reason, " +
            "a.created_time, " +
            "a.status, " +
            "a.reject_reason " +
            "FROM bill_appeals a " +
            "JOIN water_electricity_bill b ON a.bill_id = b.id " +
            "JOIN user u ON a.user_id = u.id " +
            "ORDER BY a.created_time DESC")
    List<Map<String, Object>> listAllAppeals();

    /**
     * 按状态查询账单申诉记录
     */
    @Select("SELECT a.id AS appeal_id, " +
            "u.username AS user_name, " +
            "u.building, " +
            "u.dormitory, " +
            "b.year, " +
            "b.month, " +
            "b.water_cost, " +
            "b.electricity_cost, " +
            "a.reason, " +
            "a.created_time, " +
            "a.status, " +
            "a.reject_reason " +
            "FROM bill_appeals a " +
            "JOIN water_electricity_bill b ON a.bill_id = b.id " +
            "JOIN user u ON a.user_id = u.id " +
            "WHERE a.status = #{status} " +
            "ORDER BY a.created_time DESC")
    List<Map<String, Object>> listAppealsByStatus(@Param("status") String status);

//    /**
//     * 更新账单信息
//     */
//    @Update("UPDATE water_electricity_bill " +
//            "SET water_cost = #{waterCost}, " +
//            "electricity_cost = #{electricityCost}, " +
//            "total_cost = #{totalCost} " +
//            "WHERE id = #{id}")
//    void updateBill(@Param("id") String billId,
//                    @Param("waterCost") double waterCost,
//                    @Param("electricityCost") double electricityCost,
//                    @Param("totalCost") double totalCost);

    /**
     * 批准申诉
     */
    @Update("UPDATE bill_appeals " +
            "SET status = 'APPROVED', " +
            "reject_reason = NULL, " +
            "resolved_time = NOW() " +
            "WHERE id = #{appealId}")
    void approveAppeal(@Param("appealId") String appealId);

    /**
     * 拒绝申诉
     */
    @Update("UPDATE bill_appeals " +
            "SET status = 'REJECTED', " +
            "reject_reason = #{rejectReason}, " +
            "resolved_time = NOW() " +
            "WHERE id = #{appealId}")
    void rejectAppeal(@Param("appealId") String appealId, @Param("rejectReason") String rejectReason);

    /**
     * 查询用户和账单信息
     */
    @Select("SELECT u.username, u.email, b.year, b.month, b.water_cost, b.electricity_cost " +
            "FROM user u " +
            "JOIN water_electricity_bill b " +
            "ON u.building = b.building AND u.dormitory = b.dormitory " +
            "WHERE u.id = #{userId} AND b.id = #{billId}")
    Map<String, Object> getUserAndBillInfo(@Param("userId") String userId, @Param("billId") String billId);

}