package com.hkakar.projecttracking.utils;

public class Credentials {
    private String email;
    
    private String password;
    
    public Credentials() {}

    public Credentials(String username, String password) {
        this.email = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
