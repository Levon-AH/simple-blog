package com.xfst.simpleblog.constants;

public enum RoleType {
    ROLE_USER, ROLE_ADMIN;

    public RoleType fromString(String name) {
        for (RoleType item: values()){
            if (item.name().equalsIgnoreCase(name)){
                return item;
            }
        }
        return ROLE_USER;
    }
}
