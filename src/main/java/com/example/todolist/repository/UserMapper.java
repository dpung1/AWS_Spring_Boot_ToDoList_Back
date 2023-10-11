package com.example.todolist.repository;

import com.example.todolist.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 중복체크 2가지 방법 (상황에 맞게 선택)
    public User findUserByEmail(String email);
//    public Integer getUserCountByEmail(String email);

    public Integer saveUser(User user);
}
