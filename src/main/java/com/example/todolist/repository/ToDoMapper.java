package com.example.todolist.repository;

import com.example.todolist.entity.ToDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ToDoMapper {
    public int saveToDo(ToDo todo);
    public List<ToDo> getToDoListByEmail(String email);
    public int deleteToDo(int todoId);
    public int updateToDo(ToDo todo);
}
