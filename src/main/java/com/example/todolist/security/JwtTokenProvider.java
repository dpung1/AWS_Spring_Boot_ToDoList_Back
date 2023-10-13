package com.example.todolist.security;

import com.example.todolist.entity.Authority;
import com.example.todolist.entity.Role;
import com.example.todolist.service.PrincipalDetailsService;
import com.example.todolist.entity.User;
import com.example.todolist.repository.UserMapper;
import io.jsonwebtoken.Claims;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

        User user = null;
        Authentication authentication = null;
        Claims claims = null;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (Exception e) {
            return null;
        }

        String username = claims.get("email", String.class);
        List<Object> authList = claims.get("auth", List.class);

        List<Authority> authorities = new ArrayList<>();
        authList.forEach(auth -> {
            Role role = new Role();
            role.setRoleName(((Map<String, String>) auth).get("authority"));

            Authority authority = new Authority();
            authority.setRole(role);
            authorities.add(authority);
        });

         user = User.builder()
                .email(username)
                .authorities(authorities)
                .build();

        PrincipalUser principalUser = new PrincipalUser(user);

        authentication = new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());

        return authentication;
    }
}
