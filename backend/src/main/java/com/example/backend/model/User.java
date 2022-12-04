package com.example.backend.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "UserEntity",uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
@Entity
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String username;

    private String password;

    private String role; //사용자의 롤, 예: 어드민, 일반 사용자

    private String authProvider; // 이후 OAuth에서 사용할 유저 정보 제공자 : githu

    @Builder
    public User(String id, String username, String password, String role, String authProvider) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.authProvider = authProvider;
    }
}
