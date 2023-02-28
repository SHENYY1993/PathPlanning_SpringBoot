package com.shenyy.pretendto.core.biz;

import com.alibaba.fastjson.JSONObject;
import com.shenyy.pretendto.core.model.table.EBook;

public interface BookBiz {
    EBook getEBook(Long id);

    boolean saveEBook(JSONObject info);

    boolean deleteEBook(Long id);

    boolean updateEBook(JSONObject info);
}
