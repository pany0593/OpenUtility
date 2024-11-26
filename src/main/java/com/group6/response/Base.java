package com.group6.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Base <T>{
    private Integer code;
    private String message;
    private T data;
}
