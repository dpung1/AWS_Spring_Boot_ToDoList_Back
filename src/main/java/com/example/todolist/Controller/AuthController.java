package com.example.todolist.Controller;

import com.example.todolist.service.AuthService;
import com.example.todolist.dto.SigninReqDto;
import com.example.todolist.dto.SignupReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                String fieldName = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                errorMap.put(fieldName, message);
            }
            return ResponseEntity.badRequest().body(errorMap);
        }

        if(authService.isDuplicatedEmail(signupReqDto.getEmail())) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("email", "이미 사용중인 이메일 입니다.");
            return ResponseEntity.badRequest().body(errorMap);
        }
        return ResponseEntity.ok().body(authService.insertUser(signupReqDto));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqDto signinReqDto) {

        String accessToken = authService.signinUser(signinReqDto);

        return ResponseEntity.ok().body(accessToken);
    }

    @GetMapping("/authenticated")
    public ResponseEntity<?> authenticated() {
        return ResponseEntity.ok(true);
    }
}
