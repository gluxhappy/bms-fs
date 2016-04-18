package me.glux.omd.security;

import org.springframework.security.core.GrantedAuthority;

import me.glux.omd.model.security.enums.Roles;

public class OmdGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = -1497750752667917902L;
    
    private Roles role;
    public OmdGrantedAuthority(Roles role){
        this.role=role;
    }

    @Override
    public String getAuthority() {
        return role.toString();
    }

    public Roles getRole() {
        return role;
    }
}
