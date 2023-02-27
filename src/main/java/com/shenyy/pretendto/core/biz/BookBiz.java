package com.shenyy.pretendto.core.biz;

import com.alibaba.fastjson.JSONObject;

public interface BookBiz {
    Object getEBook(Long id);

    boolean saveEBook(JSONObject info);
}
