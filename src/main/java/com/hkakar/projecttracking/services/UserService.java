package com.hkakar.projecttracking.services;


import java.util.List;
import java.util.Map;

import com.hkakar.projecttracking.entities.User;

public interface UserService {
    public Map<String, String> registerUser(User user);
    public User getUser(int userId);
    public User getUserByEmail(String email);
    public List<String> getTokens(String email);
    public Map<String, String> loginUser(String email, String password);
}
