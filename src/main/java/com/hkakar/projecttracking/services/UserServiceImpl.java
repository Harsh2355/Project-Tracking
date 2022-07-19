package com.hkakar.projecttracking.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.hkakar.projecttracking.entities.User;
import com.hkakar.projecttracking.repositories.TokenDao;
import com.hkakar.projecttracking.repositories.UserDao;

@Service
@Primary
public class UserServiceImpl implements UserService {
    
    private UserDao userDAO;
    private TokenDao tokenDAO;
    
    @Autowired
    public UserServiceImpl(UserDao theUserDao,TokenDao tokenDAO) {
        this.userDAO = theUserDao;
        this.tokenDAO = tokenDAO;
    }
    
    public UserServiceImpl() {}

    @Override
    public Map<String, String> registerUser(User user) {
        return userDAO.registerUser(user);
    }

    @Override
    public User getUser(int userId) {
        return userDAO.getUser(userId);
    }

    @Override
    public List<String> getTokens(String email) {
        return tokenDAO.getTokens(email);
    }

    @Override
    public Map<String, String> loginUser(String email, String password) {
        return userDAO.login(email, password);
    }

    @Override
    @Cacheable(value="User", key="#email")
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

}
