package com.group6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class OpenUtilityApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(OpenUtilityApplication.class, args);
    }
}