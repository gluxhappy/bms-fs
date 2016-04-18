package me.glux.omd.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("name", "admin/menu");
        return "admin/menu";
    }
}
