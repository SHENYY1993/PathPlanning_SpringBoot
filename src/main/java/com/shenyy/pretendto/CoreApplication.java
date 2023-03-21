package com.shenyy.pretendto;

import com.shenyy.pretendto.pathfactory.gui.PathFinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching //开启缓存
@SpringBootApplication
public class CoreApplication {
    public static void main(String[] args) {
//        SpringApplication.run(CoreApplication.class, args);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CoreApplication.class);
        builder.headless(false).run(args);

        //路径规划窗口
        PathFinding.getInstance();
        PathFinding.getInstance().clearMap();
        PathFinding.getInstance().initialize();
    }
}
