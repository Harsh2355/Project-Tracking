package com.hkakar.projecttracking.controllers;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkakar.projecttracking.entities.Tokens;
import com.hkakar.projecttracking.entities.User;
import com.hkakar.projecttracking.exceptions.UserAlreadyExistsException;
import com.hkakar.projecttracking.services.UserService;
import com.hkakar.projecttracking.services.UserServiceImpl;
import com.hkakar.projecttracking.utils.JWT;
import com.hkakar.projecttracking.utils.responseToken;

@RestController
@RequestMapping("/api/user")
public class HomeController {
    
    private UserService userService;
    
    @Autowired
    public HomeController(UserService theUserService) {
        this.userService = theUserService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<responseToken> register(@RequestBody User user) {
        Map<String, String> tokens = userService.registerUser(user);
        return new ResponseEntity<responseToken>(new responseToken("Bearer", 
                                                                   tokens.get("accessToken"), 
                                                                   tokens.get("refreshToken")), HttpStatus.CREATED);
    }
}
