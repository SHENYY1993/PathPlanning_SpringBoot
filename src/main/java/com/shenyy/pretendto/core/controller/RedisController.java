package com.shenyy.pretendto.core.controller;

import com.shenyy.pretendto.core.biz.RedisBiz;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Resource
    RedisBiz redisBiz;

    @GetMapping(value = "/getOne")
    public Object get() {
        System.out.println(redisBiz.get("testKey"));
        return redisBiz.get("testKey");
    }

    @PostMapping(value = "/set")
    public void set(@RequestParam(value = "key") String key,
                    @RequestParam(value = "value") Object value) {
        redisBiz.set(key, value);
    }

    @PostMapping(value = "/get")
    public Object get(@RequestParam(value = "key") String key) {
        return redisBiz.get(key);
    }

    @PostMapping(value = "/delete")
    public void delete(@RequestParam(value = "key") String key) {
        redisBiz.delete(key);
    }
}
