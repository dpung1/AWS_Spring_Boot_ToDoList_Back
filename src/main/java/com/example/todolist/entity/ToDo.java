package com.example.todolist.entity;

import com.example.todolist.dto.GetToDoListRepsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ToDo {
    private int todoId;
    private String content;
    private String email;

    public GetToDoListRepsDto toToDoListRespDto() {
        return GetToDoListRepsDto.builder()
                                .todoId(todoId)
                                .content(content)
                                .email(email)
                                .build();
    }

}
