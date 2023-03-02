package com.shenyy.pretendto.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.shenyy.pretendto.core.biz.UserBiz;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserBiz userBiz;

    @DS("master")
    @PostMapping(value = "/saveUser")
    public boolean saveUser(@RequestBody JSONObject info) {
        return userBiz.saveUser(info);
    }
}
