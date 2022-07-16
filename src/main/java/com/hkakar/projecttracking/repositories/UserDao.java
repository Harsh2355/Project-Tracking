package com.hkakar.projecttracking.repositories;

import com.hkakar.projecttracking.entities.User;

public interface UserDao {
	public String getUserByEmail(String email);
	public String registerUser(User user);
}
