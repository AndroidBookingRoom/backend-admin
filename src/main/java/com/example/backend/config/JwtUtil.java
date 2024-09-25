package com.example.backend.config;

import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by NhanNguyen on 4/22/2021
 *
 * @author: NhanNguyen
 * @date: 4/22/2021
 */
@Service
public class JwtUtil implements Serializable {
    private static Logger logger = LogManager.getLogger(JwtUtil.class);
    private String SECRET_KEY = "secret";

    private static final long serialVersionUID = 1L;

    private static final long JWT_TOKEN_VALIDITY = 60 * 60 * 8;

    //    get username from jwt token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //    retrieve expiration date from jwt token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        try {
            return claimsResolver.apply(claims);
        } catch (Exception e) {
//            throw new JwtException("ExpiredJwtException");
            logger.error(e.getMessage() + ": ExpiredJwtException");
            return null;
        }
    }

    //    for retrieving any information from token we will need the secret key
    private Claims extractAllClaims(String token) {
        try {
//            logger.info(Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody());
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            logger.error(e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error(e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error(e.getMessage());
        } catch (SignatureException e) {
            logger.error(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    //    check if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //    generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    //    while creating the token -
//    1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//    2. Sign the JWT using the HS512 algorithm and secret key.
//    3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//    compaction of the JWT to a URL-safe string
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * JWT_TOKEN_VALIDITY))
                .setIssuer("Xixon-Knight")
                .setHeaderParam("tokenType", "Bearer ")
                .setAudience("You")
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}