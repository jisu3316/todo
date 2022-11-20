package com.example.barckend.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Todo")
public class Todo {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id; // 이 오브젝트의 아이디
    private String userId; // 이 오브젝트를 생성한 유저의 아이디
    private String title; // Todo 타이틀 예) 운동 하기
    private boolean done; // true - todo를 완료한 경우(checked)

    @Builder
    public Todo(String id, String userId, String title, boolean done) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.done = done;
    }
}