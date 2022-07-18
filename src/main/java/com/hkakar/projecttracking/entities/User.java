package com.hkakar.projecttracking.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.redis.core.RedisHash;

@Entity
@RedisHash("User")
public class User implements Serializable  {
	
	private static final long serialVersionUID = 3L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private int id;
    
    @Column(name="username")
    @NotEmpty
    private String username;
    
    @Column(name="password")
    @Length(min=8, max=100)
    @NotEmpty
    private String password;
    
    @Column(name="firstName")
    @NotEmpty
    private String firstName;
    
    @Column(name="lastName")
    private String lastName;
    
    @Column(name="email")
    @NotEmpty
    @Email
    private String email;
    
    @Column(name="mobile_num")
    @NotEmpty
    @Length(min=10, max=10)
    @Digits(fraction = 0, integer = 10)
    private String mobileNum;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private Set<Tokens> tokens = new HashSet<Tokens>();
    
    public User () {}

    public User(String username, String password, String firstName, String lastName, String email, String mobileNum) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNum = mobileNum;
        this.tokens = new HashSet<Tokens>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getPassword() {
        // decrypt the password
        return password;
    }

    public void setPassword(String password) {
        // encrypt the password
        this.password = password;
    }

    public Set<Tokens> getTokens() {
        return tokens;
    }

    public void setTokens(Set<Tokens> tokens) {
        this.tokens = tokens;
    }
    
    // add token to User
    public void addToken(Tokens token) {
        this.tokens.add(token);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
                + ", lastName=" + lastName + ", email=" + email + ", mobileNum=" + mobileNum + ", tokens=" + tokens
                + "]";
    }
}
