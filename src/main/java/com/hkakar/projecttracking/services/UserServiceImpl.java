package com.hkakar.projecttracking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkakar.projecttracking.entities.User;
import com.hkakar.projecttracking.repositories.UserDao;

@Service
public class UserServiceImpl implements UserService {
    
    private UserDao userDAO;
    
    @Autowired
    public UserServiceImpl(UserDao theUserDao) {
        this.userDAO = theUserDao;
    }
    
    public UserServiceImpl() {}

    @Override
    public String registerUser(User user) {
        return userDAO.registerUser(user);
        
    }

}
