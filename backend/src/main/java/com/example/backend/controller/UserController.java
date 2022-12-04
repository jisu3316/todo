package com.example.backend.controller;

import com.example.backend.dto.ResponseDto;
import com.example.backend.dto.UserDto;
import com.example.backend.model.User;
import com.example.backend.security.TokenProvider;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody UserDto userDto) {
        try {
            if (userDto == null || userDto.getPassword() == null) {
                throw new RuntimeException("Invalid Password value");
            }

            // 요청을 이용해 저장할 유저 만들기
            User user = User.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();

            // 서비스를 이용해 레포지토리에 유저 저장
            User registerUser = userService.create(user);
            UserDto registerUserDto = UserDto.builder()
                    .id(registerUser.getId())
                    .username(registerUser.getUsername())
                    .build();

            return ResponseEntity.ok().body(registerUserDto);
        } catch (Exception e) {
            //  유저정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDto를 사용하지 않고 그냥 UserDto리턴.
            ResponseDto<Object> responseDto = ResponseDto.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticate(@RequestBody UserDto userDto) {
        User user = userService.getByCredentials(userDto.getUsername(), userDto.getPassword(), passwordEncoder);

        if (user != null) {
            final String token = tokenProvider.create(user);
            final UserDto responseUserDto = UserDto.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDto);
        } else {
            ResponseDto<Object> responseDto = ResponseDto.builder().error("Login faild").build();
            return ResponseEntity.badRequest().body(responseDto);
        }
    }
}
