package me.glux.omd.security;

import org.springframework.security.core.context.SecurityContextHolder;

public final class OmdSecurityContextHolder {
    private OmdSecurityContextHolder(){}
    public static OmdUserDetails getUserDetails(){
        OmdUserDetails userDetails = (OmdUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails;
    }
}
