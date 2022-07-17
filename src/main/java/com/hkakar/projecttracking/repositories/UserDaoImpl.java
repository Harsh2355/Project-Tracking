package com.hkakar.projecttracking.repositories;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hkakar.projecttracking.entities.Tokens;
import com.hkakar.projecttracking.entities.User;
import com.hkakar.projecttracking.exceptions.InternalServerErrorException;
import com.hkakar.projecttracking.exceptions.UserAlreadyExistsException;
import com.hkakar.projecttracking.utils.JWT;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {
    
    private EntityManager entityManager;
    
    @Value("${jwtp.secretKey}")
    private String secretKey;
    
    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String getUserByEmail(String email) {
        try {
            Session session = entityManager.unwrap(Session.class);

            Query query = session.createQuery("SELECT U.email FROM User U WHERE U.email = :user_email");
            query.setParameter("user_email", email);
            List<String> user_email = query.list();

            return !user_email.isEmpty() ? user_email.get(0) : null;
        }
        catch (Exception ex) {
            throw new InternalServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
                                                    "Ran into trouble retrieving user details");
        }
    }

    @Override
    public Map<String, String> registerUser(User user) {
        Session session = entityManager.unwrap(Session.class);
        String user_email = this.getUserByEmail(user.getEmail());
        
        if (user_email != null) {
            throw new UserAlreadyExistsException(HttpStatus.BAD_REQUEST.value(), 
                                                 HttpStatus.BAD_REQUEST.getReasonPhrase(), 
                                                 "User with the provided email already exists.");
        }
        
        JWT jwt = new JWT(secretKey);
        
        // Access Token
        // ttl -> 1 hour
        String accesstoken = jwt.createJWT(String.valueOf(user.getId()), "api/user/register", 3600000);
        
        // Refresh Token
        // ttl -> 30 days
        String refreshtoken = jwt.createJWT(String.valueOf(user.getId()), "api/user/register", 1728000000);
        
        Tokens newAccessToken = new Tokens(accesstoken);
        Tokens newRefreshToken = new Tokens(refreshtoken);
        user.addToken(newAccessToken);
        user.addToken(newRefreshToken);
        
        session.save(user);
        
        Map<String, String> body = new LinkedHashMap<>();
        body.put("refreshToken", refreshtoken);
        body.put("accessToken", accesstoken);
        return body;
    }

}
