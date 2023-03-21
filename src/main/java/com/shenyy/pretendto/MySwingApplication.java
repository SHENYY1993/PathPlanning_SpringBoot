package com.shenyy.pretendto;

import com.shenyy.pretendto.pathfactory.gui.PathFinding;

import javax.annotation.PostConstruct;

//@EnableCaching //开启缓存
//@SpringBootApplication
public class MySwingApplication {
    public static void main(String[] args) {
//        SpringApplication.run(MySwingApplication.class, args);
        //路径规划窗口
        PathFinding.getInstance();
        PathFinding.getInstance().clearMap();
        PathFinding.getInstance().initialize();
    }

    @PostConstruct
    public void init(){
        //路径规划窗口
//        PathFinding.getInstance();
//        PathFinding.getInstance().clearMap();
//        PathFinding.getInstance().initialize();
//        JFrame frame = new JFrame("My Swing Application");
//        JLabel label = new JLabel("Hello, world!");
//        frame.getContentPane().add(label);
//        frame.pack();
//        frame.setVisible(true);
    }
}
