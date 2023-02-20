package com.shenyy.pretendto.core.sal.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shenyy.pretendto.core.dal.mapper.EBookMapper;
import com.shenyy.pretendto.core.model.table.EBook;
import com.shenyy.pretendto.core.sal.EBookService;
import org.springframework.stereotype.Service;

@DS("slave_1")
@Service
public class EBookServiceImpl extends ServiceImpl<EBookMapper, EBook> implements EBookService {
}
