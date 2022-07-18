package com.hkakar.projecttracking.repositories;

import java.util.List;

import com.hkakar.projecttracking.entities.Tokens;

public interface TokenDao {
    public List<String> getTokens(int userId);
}
