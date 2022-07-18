package com.hkakar.projecttracking.repositories;

import java.util.Map;

import com.hkakar.projecttracking.entities.User;

public interface UserDao {
    public Boolean userExists(String email);
    public User getUserByEmail(String email);
    public Map<String, String> registerUser(User user);
    public User getUser(int userId);
    public Map<String, String> login(String email, String password);
}
