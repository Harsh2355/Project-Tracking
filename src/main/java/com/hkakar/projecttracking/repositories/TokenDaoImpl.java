package com.hkakar.projecttracking.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.hkakar.projecttracking.entities.Tokens;
import com.hkakar.projecttracking.exceptions.InternalServerErrorException;

@Repository
public class TokenDaoImpl implements TokenDao {
    
     private EntityManager entityManager;
     
     @Autowired
     public TokenDaoImpl(EntityManager entityManager) {
         this.entityManager = entityManager;
     }

    @Override
    public List<String> getTokens(int userId) {
        try {
            Session session = entityManager.unwrap(Session.class);
            
            Query<String> query = session.createQuery("SELECT T.token FROM User U LEFT JOIN U.tokens T WHERE U.id = :userId");
            query.setParameter("userId", userId);
            return query.list();
        }
        catch (Exception ex) {
            throw new InternalServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                                   HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
                                                   "Ran into trouble retrieving user tokens");
        }
    }

}
