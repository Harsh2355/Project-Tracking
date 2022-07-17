package com.hkakar.projecttracking.controllers;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkakar.projecttracking.entities.User;
import com.hkakar.projecttracking.services.UserService;
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
    
    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
    	System.out.println(userId);
    	return userService.getUser(Integer.parseInt(userId));
    }
}
