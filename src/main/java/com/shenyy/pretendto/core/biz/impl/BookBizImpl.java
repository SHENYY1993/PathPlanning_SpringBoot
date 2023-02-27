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
    private String prefix = EBook.class.getSimpleName();

    @Resource
    EBookService eBookService;

    @Resource
    RedisBiz redisBiz;

    @Override
    public Object getEBook(Long id) {
        Object dataCache = redisBiz.get(prefix + ":" + id);
        if (dataCache == null) {
            dataCache = eBookService.getById(id);
            if (dataCache != null) {
                redisBiz.setIfAbsent(prefix + ":" + id, (JSONObject.toJSON(dataCache)).toString());
            }
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

    @Override
    public boolean deleteEBook(Long id) {
        //删除缓存内
        redisBiz.delete(prefix + ":" + id);
        //删除数据库中的
        return eBookService.removeById(id);
    }

    @Override
    public Object updateEBook(Long id, JSONObject info) {
        return null;
    }
}
