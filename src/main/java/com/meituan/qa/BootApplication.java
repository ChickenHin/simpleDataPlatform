package com.meituan.qa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.meituan.qa")
@MapperScan("com.meituan.qa.domain.mapper")
public class BootApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);


        System.out.println("启动成功");
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}

