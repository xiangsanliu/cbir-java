package com.xiang.study.cbirjava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xiang.study.cbirjava.dao.mapper")
public class CbirJavaApplication {

    private static final String DLL_PATH;

    static {
        DLL_PATH = "C:\\Users\\xiang\\Documents\\opencv\\build\\java\\x64\\opencv_java342.dll";
        System.load(DLL_PATH);
    }

    public static void main(String[] args) {
        SpringApplication.run(CbirJavaApplication.class, args);
    }

}
