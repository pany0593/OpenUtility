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

}