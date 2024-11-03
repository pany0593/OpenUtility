package com.group6.mapper;

import com.group6.pojo.Bill;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface BillMapper {

     @Insert("INSERT INTO bill (id, year, month, days, building, dormitory, electricity_usage, electricity_cost, water_usage, water_cost, total_cost) " +
            "VALUES (#{id}, #{year}, #{month}, #{days}, #{building}, #{dormitory}, #{electricityUsage}, #{electricityCost}, #{waterUsage}, #{waterCost}, #{totalCost})")
    void insertBill(Bill bill);

    @Delete("DELETE FROM bill WHERE id = #{id}")
    void deleteBillById(String id);

    @Update("UPDATE bill SET year = #{year}, month = #{month}, days = #{days}, building = #{building}, dormitory = #{dormitory}, " +
            "electricity_usage = #{electricityUsage}, electricity_cost = #{electricityCost}, water_usage = #{waterUsage}, water_cost = #{waterCost}, total_cost = #{totalCost} " +
            "WHERE id = #{id}")
    void updateBill(Bill bill);

    @Select("SELECT * FROM bill WHERE id = #{id}")
    Bill selectBillById(String id);

    @Select("SELECT SUM(total_cost) FROM bill WHERE dormitory = #{dormitory} AND year BETWEEN #{startYear} AND #{endYear} AND month BETWEEN #{startMonth} AND #{endMonth}")
    BigDecimal countByDormitory(int dormitory, int startYear, int startMonth, int endYear, int endMonth);

    @Select("SELECT SUM(total_cost) FROM bill WHERE building = #{building} AND year BETWEEN #{startYear} AND #{endYear} AND month BETWEEN #{startMonth} AND #{endMonth}")
    BigDecimal countByBuilding(int building, int startYear, int startMonth, int endYear, int endMonth);

    @Select("SELECT SUM(total_cost) FROM bill WHERE year BETWEEN #{startYear} AND #{endYear} AND month BETWEEN #{startMonth} AND #{endMonth}")
    BigDecimal countBySchool(int startYear, int startMonth, int endYear, int endMonth);
}