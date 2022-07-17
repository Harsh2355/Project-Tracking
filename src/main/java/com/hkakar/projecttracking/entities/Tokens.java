package com.hkakar.projecttracking.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

@Entity
@RedisHash("Tokens")
public class Tokens implements Serializable {
	
	private static final long serialVersionUID = 4L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private int id;
    
    @Column(name="token")
    private String token;
    
//	@Column(name="user_id")
//	private int userId;
    
    public Tokens() {}
    
    public Tokens(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

//	public int getUserId() {
//		return userId;
//	}
//
//	public void setUserId(int userId) {
//		this.userId = userId;
//	}

//	@Override
//	public String toString() {
//		return "Tokens [id=" + id + ", token=" + token + ", userId=" + userId + "]";
//	}
}
