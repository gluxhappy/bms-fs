package me.glux.omd.dao.user;

import org.apache.ibatis.annotations.Param;

import me.glux.omd.dao.Mapper;
import me.glux.omd.model.security.enums.Roles;
import me.glux.omd.model.user.User;

@Mapper
public interface UserMapper {
    int deleteById(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectById(Long id);

    int updateByIdSelective(User record);

    int updateById(User record);
    
    User findByUsername(@Param("username")String username);
    
    Roles[] findUserRoles(@Param("userId")Long userId);

    User[] findAllUser();
}