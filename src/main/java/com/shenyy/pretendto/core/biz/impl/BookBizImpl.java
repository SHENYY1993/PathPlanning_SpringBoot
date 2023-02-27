package com.shenyy.pretendto.core.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.shenyy.pretendto.core.biz.BookBiz;
import com.shenyy.pretendto.core.biz.RedisBiz;
import com.shenyy.pretendto.core.model.table.EBook;
import com.shenyy.pretendto.core.sal.EBookService;
import com.shenyy.pretendto.utils.CommonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class BookBizImpl implements BookBiz {
    @Resource
    EBookService eBookService;

    @Resource
    RedisBiz redisBiz;

    @Override
    public Object getEBook(Long id) {
        Object dataCache = redisBiz.get("ebook:" + id);
        if (dataCache == null) {
            dataCache = eBookService.getById(id);
            redisBiz.setIfAbsent("ebook:" + id, (JSONObject.toJSON(dataCache)).toString());
        } else {
            dataCache = JSONObject.parseObject(dataCache.toString(), EBook.class);
        }
        return dataCache;
    }

    @Override
    public boolean saveEBook(JSONObject info) {
        EBook eBook = new EBook();
        if (CommonUtils.isKeyValueNotEmpty(info, "Name")) {
            eBook.setName(info.get("Name").toString());
        }
        if (CommonUtils.isKeyValueNotEmpty(info, "Doi")) {
            eBook.setDoi(new Long((Integer) info.get("Doi")));
        }
        if (CommonUtils.isKeyValueNotEmpty(info, "Author")) {
            eBook.setAuthor(info.get("Author").toString());
        }
        if (CommonUtils.isKeyValueNotEmpty(info, "Price")) {
            eBook.setPrice((Double) info.get("Price"));
        }

        return eBookService.save(eBook);
    }
}
