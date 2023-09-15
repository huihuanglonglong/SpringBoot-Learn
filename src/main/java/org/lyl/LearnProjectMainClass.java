package org.lyl;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@MapperScan()
@ServletComponentScan
@SpringBootApplication()
public class LearnProjectMainClass {

    private static Logger logger = LoggerFactory.getLogger(LearnProjectMainClass.class);

    public static void main(String[] args) {
        SpringApplication.run(LearnProjectMainClass.class, args);
    }

}
