package me.glux.omd.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import me.glux.omd.model.security.enums.Roles;
import me.glux.omd.model.user.User;
import me.glux.omd.service.redis.UserService;

@Service
public class OmdUserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private UserService userService;

    private Map<String, UserDetails> users = new HashMap<>();

    @PostConstruct
    public void init() {
        users.put("admin",createUser("admin","admin",Roles.USER,Roles.ADMIN,Roles.REDIS));
        users.put("user",createUser("user","user",Roles.USER));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OmdUserDetails details=null;
        User user=null;
        if((user=userService.findByUsername(username))!=null){
            details=new OmdUserDetails();
            details.setUsername(user.getUsername());
            details.setPassword(user.getPassword());
            Roles[] roles=userService.findUserRoles(user.getId());
            List<GrantedAuthority> adminRoles = null;
            if (roles != null && roles.length > 0) {
                adminRoles = new ArrayList<>(roles.length);
                for (Roles role : roles) {
                    adminRoles.add(new OmdGrantedAuthority(role));
                }
            } else {
                adminRoles = new ArrayList<>(0);
            }
            details.setAuthorities(adminRoles);
        }
        return details;
    }

    private UserDetails createUser(String username, String password, Roles... roles) {
        OmdUserDetails userDetail = new OmdUserDetails();
        userDetail.setUsername(username);
        userDetail.setPassword(encoder.encode(password));
        List<GrantedAuthority> adminRoles = null;
        if (roles != null && roles.length > 0) {
            adminRoles = new ArrayList<>(roles.length);
            for (Roles role : roles) {
                adminRoles.add(new OmdGrantedAuthority(role));
            }
        } else {
            adminRoles = new ArrayList<>(0);
        }
        userDetail.setAuthorities(adminRoles);
        return userDetail;
    }
}
