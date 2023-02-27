package com.shenyy.pretendto.core.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.shenyy.pretendto.core.biz.BookBiz;
import com.shenyy.pretendto.core.model.properties.Book;
import com.shenyy.pretendto.core.model.table.EBook;
import com.shenyy.pretendto.core.sal.EBookService;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    Book book;
    @Resource
    EBookService eBookService;
    @Resource
    BookBiz bookBiz;

    @GetMapping(value = "/testOk")
    public String testOk() {
        return "Test ok!";
    }

    @GetMapping(value = "/book")
    public Book getBook() {
        return book;
    }

    @DS("master")
    @PostMapping(value = "/master/getEBook")
    public EBook getEBook(@RequestParam(value = "id") Long id) {
        return (EBook) bookBiz.getEBook(id);
    }

    @DS("slave_1")
    @PostMapping(value = "/slave/getEBook")
    public EBook getEBook2(@RequestParam(value = "id") Long id) {
        return (EBook) bookBiz.getEBook(id);
    }

    @DS("master")
    @PostMapping(value = "/master/saveEBook")
    public boolean saveEBook(@RequestBody JSONObject info) {
        return bookBiz.saveEBook(info);
    }

    @DS("slave_1")
    @PostMapping(value = "/slave/saveEBook")
    public boolean saveEBook2(@RequestBody JSONObject info) {
        return bookBiz.saveEBook(info);
    }

    @DS("master")
    @PostMapping(value = "/master/deleteEBook")
    public boolean deleteEBook(@RequestParam(value = "id") Long id) {
        return bookBiz.deleteEBook(id);
    }
}
