package com.hkakar.projecttracking.controllers;

import java.util.Set;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkakar.projecttracking.entities.Tokens;
import com.hkakar.projecttracking.entities.User;

@RestController
public class HomeController {
	
	private EntityManager entityManager;
	
	@Autowired
	public HomeController(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@RequestMapping("/")
	@Transactional
	public String getHome() {
		Session session = entityManager.unwrap(Session.class);
		
		User user = new User("harsh07", "mayo2020", "Harsh", "Kakar", "harsh@gmail.com", "9235768749");
		
		Tokens token1 = new Tokens("jkh72r480t43h83t89yt7y6t345h6u6756407");
		Tokens token2 = new Tokens("jkh72r4873572589yt7y6t345h6u675640755");
		Tokens token3 = new Tokens("jkh72r480t43h83t89yt7y857636456347565");
		
		user.addToken(token1);
		user.addToken(token2);
		user.addToken(token3);
		
		session.save(user);
		
		return "Hello from Harsh";
	}
}
