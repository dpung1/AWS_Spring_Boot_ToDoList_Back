package com.example.todolist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private Integer userId;
    private String email;
    private String password;
    private String name;
    private List<Authority> authorities;

    public List<SimpleGrantedAuthority> toGrantedAuthorityList() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();

        authorities.forEach(authority ->
                simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority.getRole().getRoleName())));

        return simpleGrantedAuthorities;
    }
}
