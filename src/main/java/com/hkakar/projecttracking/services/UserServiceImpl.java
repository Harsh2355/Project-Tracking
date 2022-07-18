package com.hkakar.projecttracking.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.hkakar.projecttracking.entities.User;
import com.hkakar.projecttracking.repositories.TokenDao;
import com.hkakar.projecttracking.repositories.UserDao;

@Service
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
    @Cacheable(value="User", key="#userId")
    public User getUser(int userId) {
        return userDAO.getUser(userId);
    }

    @Override
//	@Cacheable(value="Tokens", key="#userId")
    public List<String> getTokens(int userId) {
        return tokenDAO.getTokens(userId);
    }

    @Override
    public Map<String, String> loginUser(String email, String password) {
        return userDAO.login(email, password);
    }

}
