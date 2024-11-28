package com.group6.util;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ThreadLocalUtil {
    private final ThreadLocal threadLocal=new ThreadLocal();
    public Object get(String name){
        Map<String,Object> claims = (Map<String, Object>) threadLocal.get();
        return claims.get(name);
    }

    public void set(Object value){
        threadLocal.set(value);
    }

    public void remove(){
        threadLocal.remove();
    }
}