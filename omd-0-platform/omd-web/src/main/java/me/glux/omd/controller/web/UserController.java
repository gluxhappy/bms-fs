package me.glux.omd.controller.web;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import me.glux.omd.dto.enums.Result;
import me.glux.omd.model.security.enums.Roles;
import me.glux.omd.rest.anno.JsonApi;
import me.glux.omd.rest.anno.JsonParam;
import me.glux.omd.service.redis.UserService;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @RequestMapping("/users")
    public String users(Model model){
        model.addAttribute("users", userService.getAllUser());
        return "admin/user/users";
    }
    
    @RequestMapping("/create")
    public String create(
            Model model){
        model.addAttribute("roles", EnumSet.allOf(Roles.class));
        return "admin/user/create";
    }
    
    @RequestMapping("/create.do")
    @JsonApi
    public Result doCreate(
            @JsonParam("username") String username,
            @JsonParam("alias") String alias,
            @JsonParam("password") String password,
            @JsonParam("roles") List<Roles> roles){
        return Result.SUCCESS;
    }
    
    @RequestMapping("/delete.do")
    @JsonApi
    public Result doDelete(
            @JsonParam("userId") String username){
        return Result.SUCCESS;
    }
}
