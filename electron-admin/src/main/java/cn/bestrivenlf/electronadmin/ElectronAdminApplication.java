package cn.bestrivenlf.electronadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "cn.bestrivenlf.electronadmin.dao")
public class    ElectronAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectronAdminApplication.class, args);
    }
}
