package com.example.todolist.service;

import com.example.todolist.security.JwtTokenProvider;
import com.example.todolist.dto.SigninReqDto;
import com.example.todolist.dto.SignupReqDto;
import com.example.todolist.entity.User;
import com.example.todolist.repository.UserMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;


    public Boolean isDuplicatedEmail(String email) {
        Boolean result = false;

        // 중복체크 2가지 방법 (상황에 맞게 선택)
        result = userMapper.findUserByEmail(email) != null;
//        result = userMapper.getUserCountByEmail(email) > 0;

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean insertUser(SignupReqDto signupReqDto) {
        Boolean result = false;
        User user = signupReqDto.toUserEntity(passwordEncoder);
        result = userMapper.saveUser(user) > 0;

        return result;
    }


    public String signinUser(SigninReqDto signinReqDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signinReqDto.getEmail(), signinReqDto.getPassword());

        System.out.println(authenticationToken.getName());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        return accessToken;
    }

    public Boolean authenticate(String token) {
        String accessToken = jwtTokenProvider.convertToken(token);
        if(!jwtTokenProvider.validateToken(accessToken)) {
            throw new JwtException("사용자 정보가 만교되었습니다. 다시 로그인하세요.");
        }
        return true;
    }
}
















