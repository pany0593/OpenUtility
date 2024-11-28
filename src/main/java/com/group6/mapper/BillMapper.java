package com.group6.mapper;

import com.group6.pojo.Bill;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface BillMapper {


    @Insert("INSERT INTO water_electricity_bill (id,year,month,days,building,dormitory,electricity_usage,electricity_cost,water_usage,water_cost,total_cost) " +
            "VALUES (#{bill.id}, #{bill.year}, #{bill.month}, #{bill.days}, #{bill.building}, #{bill.dormitory}, #{bill.electricity_usage}, #{bill.electricity_cost}, #{bill.water_usage}, #{bill.water_cost}, #{bill.total_cost})")
    int insertBill(@Param("bill") Bill bill);

    @Delete("DELETE FROM water_electricity_bill WHERE id = #{id}")
    int deleteBill(String id);
    @Update("UPDATE water_electricity_bill SET year = #{bill.year}, month = #{bill.month}, days = #{bill.days}, building = #{bill.building}, dormitory = #{bill.dormitory}, " +
            "electricity_usage = #{bill.electricity_usage}, electricity_cost = #{bill.electricity_cost}, water_usage = #{bill.water_usage}, water_cost = #{bill.water_cost}, total_cost = #{bill.total_cost} WHERE id = #{bill.id}")
    int updateBill(@Param("bill") Bill bill);

    @Select("SELECT * FROM water_electricity_bill WHERE id = #{bill.id}")
    Bill selectBillById(@Param("bill") Bill bill);

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