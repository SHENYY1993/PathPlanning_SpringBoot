package com.shenyy.pretendto.core.model.table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: shenyy
 * @time: 2023/2/20
 */

@Data
@TableName("pt_book")
public class EBook {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String author;
    private Long doi;
    private String description;
    private Date date;
    private Double price;
    private Date createdAt;
    private Date updatedAt;
}
