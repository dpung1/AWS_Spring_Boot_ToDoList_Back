package com.example.todolist.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetToDoListRepsDto {
    private int todoId;
    private String content;
    private String email;
}
