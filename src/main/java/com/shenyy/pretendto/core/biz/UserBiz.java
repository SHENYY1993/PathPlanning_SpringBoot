package com.shenyy.pretendto.core.biz;

import com.alibaba.fastjson.JSONObject;

public interface UserBiz {
    boolean saveUser(JSONObject info);
}
