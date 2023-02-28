package com.shenyy.pretendto.core.biz.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shenyy.pretendto.core.biz.MongoDBBiz;
import org.bson.Document;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MongoDBBizImpl implements MongoDBBiz {
    @Resource
    MongoDatabase mongoDatabase;

    @Override
    public Object get(String key) {
        MongoCollection<Document> documents = mongoDatabase.getCollection("accounts");
        Object result = documents.find().first();
        return result;
    }
}
