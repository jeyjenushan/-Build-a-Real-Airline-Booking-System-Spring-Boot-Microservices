package com.jenu.configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class JwtProvider {

    private final SecretKey secretKey =
            Keys.hmacShaKeyFor(
                 JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8)
            );


    public String generateToken(Authentication authentication,Long userId) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles=popularAuthorities(authorities);
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+86400000))
                .claim("email",authentication.getName())
                .claim("authorities",roles)
                .claim("userid",userId)
                .signWith(secretKey)
                .compact();



    }

    private String popularAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String>auths=new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }
        return String.join(",",auths);

    }
}