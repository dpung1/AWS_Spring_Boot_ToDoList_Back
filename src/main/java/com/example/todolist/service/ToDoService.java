package com.example.todolist.service;


import com.example.todolist.dto.AddToDoReqDto;
import com.example.todolist.dto.GetToDoListRepsDto;
import com.example.todolist.dto.UpdateToDoReqDto;
import com.example.todolist.entity.ToDo;
import com.example.todolist.repository.ToDoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoMapper toDoMapper;


    @Transactional(rollbackFor = Exception.class)
    public Boolean addToDo(AddToDoReqDto addToDoReqDto) {
        // JwtAuthenticationFilter에서 인증받으려고 넣어둔 Authentication을 다시 꺼내서 email 가져옴.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        ToDo todo = ToDo.builder()
                .content(addToDoReqDto.getContent())
                .email(email)
                .build();
        return toDoMapper.saveToDo(todo) > 0;
    }

    public List<GetToDoListRepsDto> getToDoList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<GetToDoListRepsDto> getToDoListRepsDtos = new ArrayList<>();

        toDoMapper.getToDoListByEmail(email).forEach(todo -> {
            getToDoListRepsDtos.add(todo.toToDoListRespDto());
        });

        return getToDoListRepsDtos;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean removeToDo(int todoId) {
        return toDoMapper.deleteToDo(todoId) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateToDo(int todoId, UpdateToDoReqDto updateToDoReq) {
        ToDo todo = ToDo.builder()
                        .todoId(todoId)
                        .content(updateToDoReq.getUpdateContent())
                        .build();
        return toDoMapper.updateToDo(todo) > 0;
    }
}
