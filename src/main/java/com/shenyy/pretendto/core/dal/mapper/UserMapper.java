package com.shenyy.pretendto.core.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shenyy.pretendto.core.model.table.Role;
import com.shenyy.pretendto.core.model.table.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select(
            "select * from user where username=#{username}"
    )
    User loadUserByUsername(String username);

    @Select(
            "select * from role r,user_role ur where r.id=ur.rid and ur.uid=#{id}"
    )
    List<Role> getUserRoleByUid(Integer id);
}
