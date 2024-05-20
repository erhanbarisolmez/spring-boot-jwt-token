package com.security.jwttoken.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class JWTService {

  @Value("${jwt.key}")
  private String SECRET;

  public String generateToken(String userName) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("can", "wia");
    log.info("CLAIMS: {}",userName);
    return createToken(claims, userName);

  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    String username = extractUser(token);
    Date expirationDate = extractExpiration(token);
    return userDetails.getUsername().equals(username) && !expirationDate.before(new Date());
  }

  public String extractUser(String token) {
    Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }

  private Date extractExpiration(String token) {
    Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.getExpiration();
  }

  private String createToken(Map<String, Object> claims, String userName) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userName)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
