package com.example.tecleadtask.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConstants {

    public static final String BASE_URL = "/api/va1/";
    public static final String SAVE_USER = "/user";
    public static final String FIND_USERS = "/users";
    public static final String FIND_USER_BY_ID = "/users/{id}";
    public static final String DELETE_USER = "/user";
    public static final String DELETE_USER_BY_ID = "/user/{id}";
    public static final String UPDATE_USER = "/user";
    public static final String FIND_USER_BY_VORNAME = "/user";
    public static final String CUSTOM_MESSAGE = "Custom Message: ";
}
