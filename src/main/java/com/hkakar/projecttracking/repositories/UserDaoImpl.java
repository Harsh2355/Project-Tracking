package com.hkakar.projecttracking.repositories;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
    
    private RedisTemplate<String, Object> template;
    
    @Value("${jwtp.secretKey}")
    private String secretKey;
    
    @Autowired
    public UserDaoImpl(EntityManager entityManager, RedisTemplate<String, Object> template) {
        this.entityManager = entityManager;
        this.template = template;
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
        
//        System.out.println(user.getId());
//        System.out.println(user);
//        template.opsForHash().put("User", (Integer) user.getId(), user);
        
        session.save(user);
        
        Map<String, String> body = new LinkedHashMap<>();
        body.put("refreshToken", refreshtoken);
        body.put("accessToken", accesstoken);
        return body;
    }

	@Override
	public User getUser(int userId) {
////		User cachedUser = (User) template.opsForHash().get("User", userId);
//		String cachedUser = (String) template.opsForHash().get("User", userId);
//		if (cachedUser != null) {
//			System.out.println(cachedUser);
//		}
		Session session = entityManager.unwrap(Session.class);
//		Query query = session.createQuery("SELECT User from User U where U.id=user_id");
//        query.setParameter("user_id", userId);
//        List<Object> users = query.list();
		System.out.println(userId);
		User user = (User) session.get(User.class, userId);
        return user;
	}

}
