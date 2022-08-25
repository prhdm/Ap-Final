package com.math.ap.winter2022.model;

public class User {

    private final String name;

    private final String password;

    protected User(String username, String password) {
        this.name = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
