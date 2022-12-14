package com.example.backend.controller;

import com.example.backend.dto.ResponseDto;
import com.example.backend.dto.TodoDto;
import com.example.backend.model.Todo;
import com.example.backend.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService service;

    @GetMapping
    public ResponseEntity<?> testTodo() {
//        String str = service.testService(); // 테스트 서비스 사용
//        List<String> list = new ArrayList<>();
//        list.add(str);
//        ResponseDto<String> response = ResponseDto.<String>builder().data(list).build();
        List<Todo> todos = service.testService();
        List<TodoDto> todoDtos = todos.stream().map(TodoDto::new).collect(Collectors.toList());
        ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(todoDtos).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDto dto) {
        try {
            // (1) TodoEntity로 변환한다.
            Todo entity = TodoDto.toEntity(dto);

            // (2) id를 null로 초기화 한다. 생성 당시에는 id가 없어야 하기 때문이다.
            entity.setId(null);

            // (3) 임시 유저 아이디를 설정 해 준다. 이 부분은 4장 인증과 인가에서 수정 할 예정이다. 지금은 인증과 인가 기능이 없으므로 한 유저(temporary-user)만 로그인 없이 사용 가능한 애플리케이션인 셈이다
            entity.setUserId(userId);

            // (4) 서비스를 이용해 Todo엔티티를 생성한다.
            List<Todo> entities = service.create(entity);

            // (5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.

            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

            // (6) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();

            // (7) ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // (8) 혹시 예외가 나는 경우 dto대신 error에 메시지를 넣어 리턴한다.

            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,@RequestBody TodoDto dto) {
        // (1) dto를 entity로 변환한다.
        Todo entity = TodoDto.toEntity(dto);

        // (2) id를 temporaryUserId로 초기화 한다. 여기는 4장 인증과 인가에서 수정 할 예정이다.
        entity.setUserId(userId);

        // (3) 서비스를 이용해 entity를 업데이트 한다.
        List<Todo> entities = service.update(entity);

        // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
        List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

        // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
        ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();

        // (6) ResponseDTO를 리턴한다.
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,@RequestBody TodoDto dto) {
        try {
            // (1) TodoEntity로 변환한다.
            Todo entity = TodoDto.toEntity(dto);

            // (2) 임시 유저 아이디를 설정 해 준다. 이 부분은 4장 인증과 인가에서 수정 할 예정이다. 지금은 인증과 인가 기능이 없으므로 한 유저(temporary-user)만 로그인 없이 사용 가능한 애플리케이션인 셈이다
            entity.setUserId(userId);

            // (3) 서비스를 이용해 entity를 삭제 한다.
            List<Todo> entities = service.delete(entity);

            // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

            // (5) 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화한다.
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();

            // (6) ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // (8) 혹시 예외가 나는 경우 dto대신 error에 메시지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}