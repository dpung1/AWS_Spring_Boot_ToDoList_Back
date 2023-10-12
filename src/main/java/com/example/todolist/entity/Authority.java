package com.example.todolist.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Authority {
    private int authorityId;
    private int userId;
    private int roleId;
    private Role role;
}
