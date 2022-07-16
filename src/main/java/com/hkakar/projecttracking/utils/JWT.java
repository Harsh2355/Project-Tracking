package com.hkakar.projecttracking.utils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.Key;

import io.jsonwebtoken.*;

import java.util.Date;

@Component
public class JWT {	
    
    private String SECRET_KEY;
    
    public JWT() {}
    
    public JWT(String secretKey) {
        this.SECRET_KEY = secretKey;
    }
    
    public String createJWT(String id, String issuer, long ttl) {
        
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        
        long curMilliseconds = System.currentTimeMillis();
        Date today = new Date(curMilliseconds);
        
        byte[] apiSecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiSecretBytes, signatureAlgorithm.getJcaName());
        
        JwtBuilder builder = Jwts.builder()
                                 .setId(id)
                                 .setIssuedAt(today)
                                 .setIssuer(issuer)
                                 .signWith(signingKey, signatureAlgorithm);
        
        if (ttl >= 0) {
            long expiry = curMilliseconds + ttl;
            Date exp = new Date(expiry);
            builder.setExpiration(exp);
        }
        
        return builder.compact();
    }
    
    public Jws<Claims> decodeJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Jws<Claims> claims = Jwts.parserBuilder()
                            .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                            .build()
                            .parseClaimsJws(jwt);
        return claims;
    }
}
