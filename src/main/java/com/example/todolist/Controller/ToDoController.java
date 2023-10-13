package com.example.todolist.Controller;

import com.example.todolist.dto.AddToDoReqDto;
import com.example.todolist.dto.UpdateToDoReqDto;
import com.example.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    @PostMapping("/todo")
    public ResponseEntity<?> addToDo(@RequestBody AddToDoReqDto addToDoReqDto) {
        return ResponseEntity.ok(toDoService.addToDo(addToDoReqDto));
    }

    @GetMapping("/todo/list")
    public ResponseEntity<?> getToDoList() {
        return ResponseEntity.ok().body(toDoService.getToDoList());
    }

    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<?> removeToDo(@PathVariable int todoId) {
        return ResponseEntity.ok().body(toDoService.removeToDo(todoId));
    }

    @PutMapping("/todo/{todoId}")
    public ResponseEntity<?> updateToDo(@PathVariable int todoId, @RequestBody UpdateToDoReqDto updateToDoReqDto) {
        return ResponseEntity.ok().body(toDoService.updateToDo(todoId, updateToDoReqDto));
    }
}
