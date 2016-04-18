package me.glux.omd.model.web.enums;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import me.glux.omd.model.security.enums.Roles;

public enum Functions {
    Redis("Redis","/redis/menu",Roles.REDIS),
    Admin("Admin","/admin/menu",Roles.ADMIN);
    
    private String desp;
    private String url;
    private Roles role;
    
    private Functions(String desp,String url,Roles role){
        this.desp=desp;
        this.url=url;
        this.role=role;
    }

    public String getDesp() {
        return desp;
    }

    public String getUrl() {
        return url;
    }
    
    private Roles getRole() {
        return role;
    }

    public static Set<Functions> getAllAccessabeFunctions(Collection<Roles> hasRoles){
        Set<Functions> result=new TreeSet<>();
        Set<Roles> roles=(hasRoles==null?new HashSet<Roles>():new HashSet<>(hasRoles));
        for(Functions function:EnumSet.allOf(Functions.class)){
            if(function.getRole() == null || roles.contains(function.getRole())){
                result.add(function);
            }
        }
        return result;
    }
}
