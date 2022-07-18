package com.hkakar.projecttracking.utils;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class UpdatableBcrypt {
    private final int logRounds;
    
    public UpdatableBcrypt(int logRounds) {
        this.logRounds = logRounds;
    }
    
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }
    
    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
