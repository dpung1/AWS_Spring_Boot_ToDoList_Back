package com.example.todolist.security;

import com.example.todolist.service.PrincipalDetailsService;
import com.example.todolist.entity.User;
import com.example.todolist.repository.UserMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final PrincipalDetailsService principalService;
    private final UserMapper userMapper;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Autowired PrincipalDetailsService principalService,
                            @Autowired UserMapper userMapper) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.principalService = principalService;
        this.userMapper = userMapper;
    }

    public String generateAccessToken(Authentication authentication) {
        String accessToken = null;

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();

        Date tokenExpiresDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
        JwtBuilder jwtBuilder = Jwts.builder()
                                    .setSubject("AccessToken")
                                    .claim("auth", authentication.getAuthorities())
                                    .setExpiration(tokenExpiresDate)
                                    .signWith(key, SignatureAlgorithm.HS256);

        User user = userMapper.findUserByEmail(principalUser.getUsername());

        if(user != null) {
            return jwtBuilder.claim("email", user.getEmail()).compact();
        }
        return  jwtBuilder.claim("email", principalUser.getUsername()).compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String convertToken(String bearerToken) {
        String type = "Bearer ";
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(type)) {
            return bearerToken.substring(type.length());
        }
        return "";
    }

    public Authentication getAuthentication(String accessToken) {

        Authentication authentication = null;

        String username = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(accessToken)
                            .getBody()
                            .get("email")
                            .toString();

        PrincipalUser principalUser = (PrincipalUser) principalService.loadUserByUsername(username);

        authentication = new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());

        return authentication;
    }
}
