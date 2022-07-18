package com.hkakar.projecttracking.controllers;

import java.util.LinkedHashMap;
import java.util.List;
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
import com.hkakar.projecttracking.utils.Credentials;
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
    public Map<String, Object> getUser(@PathVariable String userId) {
        System.out.println(userId);
        User user = userService.getUser(Integer.parseInt(userId));
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", user.getUsername());
        body.put("email", user.getEmail());
        body.put("firstName", user.getFirstName());
        body.put("lastName", user.getLastName());
        body.put("mobileNum", user.getMobileNum());
        return body;
    }
    
    @PostMapping("/login")
    public ResponseEntity<responseToken> login(@RequestBody Credentials creds) {
        Map<String, String> tokens = userService.loginUser(creds.getEmail(), creds.getPassword());
        return new ResponseEntity<responseToken>(new responseToken("Bearer", 
                                                                   tokens.get("accessToken"), 
                                                                   tokens.get("refreshToken")), HttpStatus.OK);
    }
    
    @GetMapping("/tokens/{userId}")
    public List<String> getUserTokens(@PathVariable String userId) {
        return userService.getTokens(Integer.parseInt(userId));
    }
    
}
