package com.example.spring_friends.dto;

// MemberDTO: 폼데이터의 입력 및 전달을 위한 Class. 회원가입 시, 필요한 데이터 변수 정의

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;


@ToString
@Getter
@Setter
public class MemberDTO {

    private long id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private Timestamp joinDate;
    private String role;

}
