package com.udise.portal.vo.user;

import com.udise.portal.entity.AppUser;
import com.udise.portal.enums.Role;

public class AppUserGetAllVo {
    public static final String ROLE = "role";

    public static final String NAME = "name";

    public static final String MODULE = "module";

    private Long id;

    private String name;

    private String email;

    private String mobile;

    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void toConvertVo(AppUser current) {
        /************** For AppUser List **************/
        setId(current.getId());
        setName(current.getFullName());
        setEmail(current.getEmail());
        setMobile(current.getMobile());
        setRole(current.getRole());
    }
}
