package me.glux.omd.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import me.glux.omd.security.OmdSecurityContextHolder;

@Controller
public class MainController {
    
    @RequestMapping(value="/main")
    public String main(Model model){
        model.addAttribute("name", OmdSecurityContextHolder.getUserDetails().getUsername());
        return "main";
    }

    @RequestMapping(value="/menu")
    public String menu(Model model){
        return "menu";
    }
}
