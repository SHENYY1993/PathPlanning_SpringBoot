package com.shenyy.pretendto.core.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.shenyy.pretendto.core.model.table.Book;
import com.shenyy.pretendto.core.model.table.EBook;
import com.shenyy.pretendto.core.sal.EBookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    Book book;
    @Resource
    EBookService eBookService;

    @GetMapping(value = "/testOk")
    public String testOk() {
        return "Test ok!";
    }

    @GetMapping(value = "/book")
    public Book getBook() {
        return book;
    }

    @DS("master")
    @GetMapping(value = "/master/ebook")
    public EBook getEBook() {
        return eBookService.getById(1L);
    }

    @DS("slave_1")
    @GetMapping(value = "/slave/ebook")
    public EBook getEBook2() {
        return eBookService.getById(2L);
    }
}
