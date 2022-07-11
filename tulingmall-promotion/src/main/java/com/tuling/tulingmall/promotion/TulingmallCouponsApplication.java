package com.tuling.tulingmall.promotion;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableFeignClients
public class TulingmallCouponsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TulingmallCouponsApplication.class, args);
	}

}
