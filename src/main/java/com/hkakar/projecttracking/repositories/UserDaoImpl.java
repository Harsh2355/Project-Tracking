package com.hkakar.projecttracking.repositories;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hkakar.projecttracking.entities.Tokens;
import com.hkakar.projecttracking.entities.User;
import com.hkakar.projecttracking.exceptions.InternalServerErrorException;
import com.hkakar.projecttracking.exceptions.UserAlreadyExistsException;
import com.hkakar.projecttracking.utils.JWT;
import com.hkakar.projecttracking.utils.UpdatableBcrypt;

@Repository
@Transactional
@Primary
public class UserDaoImpl implements UserDao {
    
    private EntityManager entityManager;
    
    private JWT jwt;
    
    private UpdatableBcrypt bcrypt = new UpdatableBcrypt(11);
    
    @Value("${jwtp.secretKey}")
    private String secretKey;
    
    @Autowired
    public UserDaoImpl(EntityManager entityManager, JWT jwt) {
        this.entityManager = entityManager;
        this.jwt = jwt;
    }

    @Override
    public Boolean userExists(String email) {
        try {
            Session session = entityManager.unwrap(Session.class);

            Query query = session.createQuery("SELECT U.email FROM User U WHERE U.email = :user_email");
            query.setParameter("user_email", email);
            List<String> user_email = query.list();

            return !user_email.isEmpty();
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
        Boolean userExists = this.userExists(user.getEmail());
        
        if (userExists) {
            throw new UserAlreadyExistsException(HttpStatus.BAD_REQUEST.value(), 
                                                 HttpStatus.BAD_REQUEST.getReasonPhrase(), 
                                                 "User with the provided email already exists.");
        }
        
        // Access Token
        // ttl -> 1 hour
        String accesstoken = jwt.createJWT(String.valueOf(user.getId()), user.getEmail(), 3600000);
        
        // Refresh Token
        // ttl -> 30 days
        String refreshtoken = jwt.createJWT(String.valueOf(user.getId()), "api/user/register", 1728000000);
        
        Tokens newAccessToken = new Tokens(accesstoken);
        Tokens newRefreshToken = new Tokens(refreshtoken);
        user.addToken(newAccessToken);
        user.addToken(newRefreshToken);
        
        // hash the password
        user.setPassword(bcrypt.hash(user.getPassword()));
        
        try {
            session.save(user);	
        }
        catch (Exception ex) {
            throw new InternalServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                                   HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
                                                   "Ran into trouble saving user details");
        }
      
        
        Map<String, String> body = new LinkedHashMap<>();
        body.put("refreshToken", refreshtoken);
        body.put("accessToken", accesstoken);
        return body;
    }

    @Override
    public User getUser(int userId) {
        Session session = entityManager.unwrap(Session.class);
        User user = (User) session.get(User.class, userId);
        return user;
    }

    @Override
    public Map<String, String> login(String email, String password) {
        Session session = entityManager.unwrap(Session.class);
        Boolean userExists = this.userExists(email);
        
        if (!userExists) {
            throw new UserAlreadyExistsException(HttpStatus.BAD_REQUEST.value(), 
                                                 HttpStatus.BAD_REQUEST.getReasonPhrase(), 
                                                 "Incorrect Email Address: No Such user exists");
        }
        
        User user = this.getUserByEmail(email);
        if (bcrypt.verifyHash(password, user.getPassword())) {
            
            // Access Token
            // ttl -> 1 hour
            String accesstoken = jwt.createJWT(String.valueOf(user.getId()), "api/user/login", 3600000);
            
            // Refresh Token
            // ttl -> 30 days
            String refreshtoken = jwt.createJWT(String.valueOf(user.getId()), "api/user/login", 1728000000);
            
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
        else {
            throw new UserAlreadyExistsException(HttpStatus.BAD_REQUEST.value(), 
                    HttpStatus.BAD_REQUEST.getReasonPhrase(), 
                    "Incorrect Password");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("FROM User U WHERE U.email = :user_email", User.class);
        query.setParameter("user_email", email);
        List<User> user = query.list();
        return (!user.isEmpty()) ? user.get(0) : null;
    }

}
