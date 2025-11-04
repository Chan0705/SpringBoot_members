package com.example.spring_friends.entity;

// entity => db 연동값 설정

import com.example.spring_friends.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;


@Table(name = "t_member")
@Data // getter, setter, toString 통합 적용 어노테이션
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true) // 중복방지
    private String email;
    @Column(nullable = false) // 필수입력
    private String Password;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 10)
    private String gender;
    @CreationTimestamp
    private Timestamp joinDate;
    @Column(nullable = false)
    private String role; // 권한

    // dto를 entity로 변환하는 메서드 정의
    public static Member toSaveEntity(MemberDTO dto, PasswordEncoder pwEncoder){
        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setPassword(pwEncoder.encode(dto.getPassword()));
        member.setName(dto.getName());
        member.setGender(dto.getGender());
        member.setRole(dto.getRole());

        return member;
    }

}
