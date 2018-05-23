package com.keyfalcon.budget;

public enum Role{
    SUPERADMIN,
    ADMIN,
    USER;

    public static Role getValue(Long i) {
        if (i == 1) {
            return SUPERADMIN;
        }else if (i == 2) {
            return ADMIN;
        }
        return USER;
    }
}
