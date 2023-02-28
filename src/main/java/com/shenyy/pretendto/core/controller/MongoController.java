package com.shenyy.pretendto.core.controller;

import com.shenyy.pretendto.core.biz.MongoDBBiz;
import com.shenyy.pretendto.core.biz.RedisBiz;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mongo")
public class MongoController {
    @Resource
    MongoDBBiz mongoDBBiz;

    @PostMapping(value = "/get")
    public Object get(@RequestParam(value = "key") String key) {
        return mongoDBBiz.get(key);
    }

//    @PostMapping(value = "/delete")
//    public void delete(@RequestParam(value = "key") String key) {
//        mongoDBBiz.delete(key);
//    }
}
