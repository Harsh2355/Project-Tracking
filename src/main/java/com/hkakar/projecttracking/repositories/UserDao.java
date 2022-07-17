package com.hkakar.projecttracking.repositories;

import java.util.Map;

import com.hkakar.projecttracking.entities.User;

public interface UserDao {
    public String getUserByEmail(String email);
    public Map<String, String> registerUser(User user);
    public User getUser(int userId);
}
