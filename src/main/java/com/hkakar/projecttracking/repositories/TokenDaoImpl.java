package com.hkakar.projecttracking.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.hkakar.projecttracking.entities.User;
import com.hkakar.projecttracking.exceptions.InternalServerErrorException;

@Repository
@Primary
public class TokenDaoImpl implements TokenDao {
    
     private EntityManager entityManager;
     
     @Autowired
     public TokenDaoImpl(EntityManager entityManager) {
         this.entityManager = entityManager;
     }

    @Override
    public List<String> getTokens(String email) {
        try {
            Session session = entityManager.unwrap(Session.class);
            
            Query query = session.createQuery("FROM User U WHERE U.email = :user_email", User.class);
            query.setParameter("user_email", email);
            List<User> users = query.list();
            if (users.isEmpty()) {
                throw new Exception();
            }
            User user = users.get(0);
            
            Query<String> newquery = session.createQuery("SELECT T.token FROM User U LEFT JOIN U.tokens T WHERE U.id = :userId");
            newquery.setParameter("userId", user.getId());
            return newquery.list();
        }
        catch (Exception ex) {
            throw new InternalServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                                   HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
                                                   "Ran into trouble retrieving user tokens");
        }
    }

}
