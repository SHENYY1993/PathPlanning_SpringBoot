package com.shenyy.pretendto.core.biz;

import com.alibaba.fastjson.JSONObject;

public interface BookBiz {
    Object getEBook(Long id);

    boolean saveEBook(JSONObject info);

    boolean deleteEBook(Long id);

    Object updateEBook(Long id, JSONObject info);
}
