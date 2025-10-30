package com.example.spring_friends.dto;

// Form에 있는 data를 전달하는 역할
// 게시판 내용 data 정의

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;


@Getter
@Setter
@ToString
public class BoardDTO {
    
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Timestamp regDate;
    private Integer hits; // 조회수, 이렇게 하면 새 게시글 생성 시 자동으로 0으로 초기화됨

}
