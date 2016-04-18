package me.glux.omd.service.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.glux.omd.dao.user.UserMapper;
import me.glux.omd.model.security.enums.Roles;
import me.glux.omd.model.user.User;
import me.glux.omd.service.redis.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public User findByUsername(String username) {
        User result=null;
        if(null != username){
            result = userMapper.findByUsername(username);
        }
        return result;
    }

    @Override
    public Roles[] findUserRoles(Long userId) {
        Roles[] result=null;
        if(userId == null){
            result=new Roles[]{};
        }else{
            result=userMapper.findUserRoles(userId);
        }
        return result;
    }

    @Override
    public User[] getAllUser() {
        return userMapper.findAllUser();
    }

}
