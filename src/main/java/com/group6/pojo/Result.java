package com.group6.pojo;

import com.group6.response.Base;
import java.lang.Integer;
import java.lang.String;
import java.lang.Object;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Base base;
    private T data;

    public static <E> Result<E> success(E data) {
        return new Result<>(new Base(0, "操作成功"),data);
    }


    public static Result success() {
        return new Result(new Base(0, "操作成功"),null);
    }

    public static Result error(String message) {
        return new Result(new Base(-1, message),null);
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
