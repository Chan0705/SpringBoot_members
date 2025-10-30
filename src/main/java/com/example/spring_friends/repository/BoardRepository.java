package com.example.spring_friends.repository;

import com.example.spring_friends.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

// = BoardDAO

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 조회수 증가
    // @Query(value=sql 구문)
    // @Query는 Spring Data JPA에서 직접 SQL 또는 JPQL을 작성해서 원하는 쿼리를 실행할 수 있게 해주는 어노테이션
    // @Modifying : Data 삽입 및 수정 등 Data의 변경이 필요할 때 사용하는 어노테이션
    @Modifying
    @Query(value ="update Board b set b.hits=b.hits+1 where b.id=:id")
    void updateHits(Long id);


    // 제목 검색어가 포함된 게시글 목록을 처리하여 조회
    // select * from board where title like?
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    // 글 내용에 특정 검색어가 포함된 게시글 목록을 처리하여 조회
    Page<Board> findByContentContaining(String keyword, Pageable pageable);

}
