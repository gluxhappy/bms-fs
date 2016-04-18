package me.glux.omd.service.redis;

import org.springframework.stereotype.Service;

import me.glux.omd.model.security.enums.Roles;
import me.glux.omd.model.user.User;

@Service
public interface UserService {
    String getName();
    User findByUsername(String username);
    Roles[] findUserRoles(Long userId);
    User[] getAllUser();
}
