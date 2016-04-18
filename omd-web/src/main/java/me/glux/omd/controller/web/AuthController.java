package me.glux.omd.controller.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @RequestMapping({"/","/auth/login"})
    public String login(@RequestParam(value="error",defaultValue="false") boolean error,Model model){
        if(error){
            model.addAttribute("error", true);
        }
        return "/auth/login";
    }    
    @RequestMapping("/auth/logou")
    public String logout(){
        return "/auth/logout";
    }  
    @RequestMapping("/auth/denied")
    public String denied(HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_OK);
        return "/auth/denied";
    }
}
