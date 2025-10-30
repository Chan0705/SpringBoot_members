package com.example.spring_friends.entity;

// 게시글 DB저장

import com.example.spring_friends.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;


@Data // getter setter toString 전부 포함
@Table(name = "t_board") // t_board라는 Data Talbe 생성
@Entity
public class Board {

    // 아래 항목의 column을 table에 생성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key 지정
    private Long id;
    
    @Column(nullable = false)
    private String title;

    @Column(length = 4000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @CreationTimestamp // 자동생성 날짜
    private Timestamp regDate;

    @Column(columnDefinition = "Integer default 0")
    // = view_count INTEGER DEFAULT 0 : viewC가 0으로 들어오면 자동으로 1 증가
    private Integer hits; // 조회수


    // dto를 entity로 변환하는 메서드 작성
    public static Board toSaveEntity(BoardDTO dto){
        Board board = new Board();
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setHits(0); // dto.getHits를 하면 null 값으로 들어감, 0으로 디폴트값 설정
        board.setWriter(dto.getWriter());

        return board;
    }


}
