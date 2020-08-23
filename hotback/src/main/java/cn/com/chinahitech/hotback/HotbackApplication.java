package cn.com.chinahitech.hotback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.com.chinahitech.hotback.*.mapper")
public class HotbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotbackApplication.class, args);
    }

}
