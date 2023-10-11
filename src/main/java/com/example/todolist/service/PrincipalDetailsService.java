package com.example.todolist.service;

import com.example.todolist.security.PrincipalUser;
import com.example.todolist.entity.User;
import com.example.todolist.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userMapper.findUserByEmail(email);
        if(user != null) {
            return  new PrincipalUser(user);
        }
        throw new UsernameNotFoundException("잘못된 사용자 정보입니다. 다시 확인해주세요.");
    }
}
