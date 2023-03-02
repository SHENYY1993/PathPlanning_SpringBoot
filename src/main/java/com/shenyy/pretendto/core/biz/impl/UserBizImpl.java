package com.shenyy.pretendto.core.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.shenyy.pretendto.core.biz.UserBiz;
import com.shenyy.pretendto.core.model.table.User;
import com.shenyy.pretendto.core.sal.UserService;
import com.shenyy.pretendto.utils.CommonUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserBizImpl implements UserBiz {
    @Resource
    UserService userService;

    @Override
    public boolean saveUser(JSONObject info) {
        User user = new User();

        if (CommonUtils.isKeyValueNotEmpty(info, "username")) {
            if (CommonUtils.isKeyValueNotEmpty(info, "password")) {
                user.setUsername(info.get("username").toString());
                user.setPassword(new BCryptPasswordEncoder(10).encode(info.get("password").toString()));
                user.setEnabled(true);
                user.setLocked(false);
            }
        }
        return userService.save(user);
    }
}
