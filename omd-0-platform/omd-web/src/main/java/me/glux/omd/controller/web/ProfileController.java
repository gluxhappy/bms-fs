package me.glux.omd.controller.web;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import me.glux.omd.model.security.enums.Roles;
import me.glux.omd.model.web.enums.Functions;
import me.glux.omd.security.OmdGrantedAuthority;
import me.glux.omd.security.OmdSecurityContextHolder;
import me.glux.omd.security.OmdUserDetails;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @RequestMapping(value = "/info")
    public String main(Model model) {
        OmdUserDetails details = OmdSecurityContextHolder.getUserDetails();
        Set<Functions>  functions=Functions.getAllAccessabeFunctions(getAllRoles(details));
        model.addAttribute("functions", functions);
        model.addAttribute("name", details.getUsername());
        return "profile/info";
    }

    private static Set<Roles> getAllRoles(OmdUserDetails details) {
        Set<Roles> result = new TreeSet<>();
        if (null == details || details.getAuthorities() == null || details.getAuthorities().size() == 0) {
            // 
        } else {
            for (GrantedAuthority auth : details.getAuthorities()) {
                if (!OmdGrantedAuthority.class.isInstance(auth)) {
                    continue;
                }
                OmdGrantedAuthority omdAuth = (OmdGrantedAuthority) auth;
                result.add(omdAuth.getRole());
            }
        }
        return result;
    }
}
