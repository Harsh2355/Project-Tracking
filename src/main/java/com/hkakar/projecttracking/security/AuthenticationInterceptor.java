package com.hkakar.projecttracking.security;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hkakar.projecttracking.entities.Tokens;
import com.hkakar.projecttracking.entities.User;
import com.hkakar.projecttracking.exceptions.InternalServerErrorException;
import com.hkakar.projecttracking.services.UserService;
import com.hkakar.projecttracking.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JWT jwt;
    
    @Autowired
    private UserService userService;
    
    @Override
    public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        if (!request.getServletPath().contains("api/user/register") &&
                !request.getServletPath().contains("api/user/login")) {
            
            String accessToken = request.getHeader("Authorization");
            if (accessToken == null) {
                throw new InternalServerErrorException(HttpStatus.BAD_REQUEST.value(),
                                                       HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                                       "Please provide access token");
            }
            
            try {
                Jws<Claims> decoded = jwt.decodeJWT(accessToken);
                if (decoded == null) {
                    throw new Exception();
                }
                Claims claims = decoded.getBody();
                String email = (String) claims.get("iss");
                
                User user = userService.getUserByEmail(email);
                Set<Tokens> tokens = user.getTokens();
                if (!this.findToken(tokens, accessToken)) {
                    throw new InternalServerErrorException(HttpStatus.BAD_REQUEST.value(),
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            "Access Token is invalid");
                }
                request.setAttribute("user", user);
            }
            catch (Exception ex) {
                throw new InternalServerErrorException(HttpStatus.BAD_REQUEST.value(),
                                                       HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                                       "Unauthorized: accessToken is not valid");
            }
        }
        return true;
    }
    
    @Override
    public void postHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler, 
        ModelAndView modelAndView) throws Exception {
        System.out.println("Post Handle method is Calling");
    }
    
    public Boolean findToken(Set<Tokens> tokens, String userToken) {
        for (Tokens t: tokens) {
            if (t.getToken().equals(userToken)) {
                return true;
            }
        }
        return false;
    }
}
