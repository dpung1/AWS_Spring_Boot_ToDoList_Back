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

    // React 추가버튼 클릭시 넘어오는 데이터를 AddToDoReqDto로 받음(현재, content만 있음)
    @PostMapping("/todo")
    public ResponseEntity<?> addToDo(@RequestBody AddToDoReqDto addToDoReqDto) {
        return ResponseEntity.ok(toDoService.addToDo(addToDoReqDto));
    }

    // React에서 받을 데이터 매개변수 X 대신 보내야 하는 데이터가 있으므로 ResponseDto를 생성
    @GetMapping("/todo/list")
    public ResponseEntity<?> getToDoList() {
        return ResponseEntity.ok().body(toDoService.getToDoList());
    }

    // 프라이머리키를 기준으로 해당되는 todoid를 받음
    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<?> removeToDo(@PathVariable int todoId) {
        return ResponseEntity.ok().body(toDoService.removeToDo(todoId));
    }

    // delete와 동일하게 todoid를 받고 추가활때 처럼 updateToDoReqDto로 받음
    @PutMapping("/todo/{todoId}")
    public ResponseEntity<?> updateToDo(@PathVariable int todoId, @RequestBody UpdateToDoReqDto updateToDoReqDto) {
        return ResponseEntity.ok().body(toDoService.updateToDo(todoId, updateToDoReqDto));
    }
}
