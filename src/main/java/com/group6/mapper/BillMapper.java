package com.group6.mapper;

import com.group6.pojo.Bill;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface BillMapper {


    @Insert("INSERT INTO bill (id, year, month, days, building, dormitory, electricity_usage, electricity_cost, water_usage, water_cost, total_cost) " +
            "VALUES (#{id}, #{year}, #{month}, #{days}, #{building}, #{dormitory}, #{electricityUsage}, #{electricityCost}, #{waterUsage}, #{waterCost}, #{totalCost})")
    int insertBill(Bill bill);

    @Delete("DELETE FROM bill WHERE id = #{id}")
    int deleteBill(@Param("id") String id);
    @Update("UPDATE bill SET year = #{year}, month = #{month}, days = #{days}, building = #{building}, dormitory = #{dormitory}, " +
            "electricity_usage = #{electricityUsage}, electricity_cost = #{electricityCost}, water_usage = #{waterUsage}, water_cost = #{waterCost}, total_cost = #{totalCost} WHERE id = #{id}")
    int updateBill(Bill bill);

    @Select("SELECT * FROM bill WHERE id = #{id}")
    Bill selectBillById(@Param("id") String id);

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
}