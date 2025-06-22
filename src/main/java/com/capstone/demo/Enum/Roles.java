package com.capstone.demo.Enum;

public enum Roles {
    MEMEBR,
    PHARMACIST,
    ADMIN;


    public String[] split(String s) {
        return new String[]{"[MEMBER , PHARMACIST, ADMIN]"};
    }
}