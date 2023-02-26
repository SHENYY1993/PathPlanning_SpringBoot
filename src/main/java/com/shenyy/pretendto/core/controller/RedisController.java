package com.shenyy.pretendto.core.controller;

import com.shenyy.pretendto.core.biz.RedisBiz;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Resource
    RedisBiz redisBiz;

    @PostMapping(value = "/executeRedisCliCommand")
    public Object executeRedisCliCommand(@RequestParam(value = "command") String command,
                                         @RequestParam(value = "var1") String var1) {
        String[] strings = var1.split(" ");
        List<byte[]> arrayList = new ArrayList<>();
        for (String str :
                strings) {
            arrayList.add(str.getBytes());
        }

        // 实现对redis-cli的操作
        Object result = null;
        if (arrayList.size() == 1) {
            result = redisBiz.executeRedisCliCommand(command, arrayList.get(0));
        } else if (arrayList.size() == 2) {
            result = redisBiz.executeRedisCliCommand(command, arrayList.get(0), arrayList.get(1));
        }
        return result;
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
