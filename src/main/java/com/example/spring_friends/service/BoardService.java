package com.example.spring_friends.service;

import com.example.spring_friends.dto.BoardDTO;
import com.example.spring_friends.entity.Board;
import com.example.spring_friends.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor // 생성자 주입 - final 사용
@Service // bean 등록
public class BoardService {

    // @RequiredArgsConstructor : 생성자 주입 - final 사용 해야함
    private final BoardRepository repository;

    // 글쓰기
    public void save(BoardDTO dto){
        
        // dto => entity 변환 메서드 호출
        Board board = Board.toSaveEntity(dto);

        repository.save(board);
    }

    public List<Board> findAll() {
        return repository.findAll();
    }

    // 게시글 상세보기
    public Board findById(Long id) {
        //id값이 없는 경우에도 구현
        Board board = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        
        return board;
    }

    // 조회수 증가
    @Transactional
    // 2개 이상의 구현 시 필수 어노테이션
    // 트랜잭션(조회수, 상세보기 등 2개 이상의 기능 구현)
    public void updateHits(Long id) {
        
        repository.updateHits(id);
        
    }

    //글 삭제
    public void delete(Long id) {
        //제공된 deleteById() 사용
        repository.deleteById(id);
    }

    //글 수정
    public void update(BoardDTO dto) {
        //id에 해당하는 게시글이 가져오기
        Board board = repository.findById(dto.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        //제목, 내용 수정
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        repository.save(board);  //수정후 다시 저장
    }

    //글 목록(페이지 처리)
    public Page<Board> findAll(Pageable pageable) {
        //0-> 첫페이지, 10->페이지당 개수
        //pageable = PageRequest.of(0, 10); //오름차순
        int page = pageable.getPageNumber() - 1;
        int pageSize = 10;

        log.info("pageable.getPageNumber(): " + pageable.getPageNumber());

        //http://localhost:8080/boards/pages?page=3 ->3페이지
        //pageable = PageRequest.of(page, pageSize); //오름차순
        pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id"); //내림차순


        Page<Board> boardList = repository.findAll(pageable);
        return boardList;
    }


    // 글 제목 검색
    public Page<Board> findByTitleContaining(String keyword, Pageable pageable) {

        //0-> 첫페이지, 10->페이지당 개수
        //pageable = PageRequest.of(0, 10); //오름차순
        int page = pageable.getPageNumber() - 1;
        int pageSize = 10;

        log.info("pageable.getPageNumber(): " + pageable.getPageNumber());

        pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id"); //내림차순


        Page<Board> boardList = repository.findByTitleContaining(keyword, pageable);
        return boardList;
    }

    // 글 내용 검색
    public Page<Board> findByContentContaining(String keyword, Pageable pageable) {

        //0-> 첫페이지, 10->페이지당 개수
        //pageable = PageRequest.of(0, 10); //오름차순
        int page = pageable.getPageNumber() - 1;
        int pageSize = 10;

        log.info("pageable.getPageNumber(): " + pageable.getPageNumber());

        //http://localhost:8080/boards/pages?page=3 ->3페이지
        //pageable = PageRequest.of(page, pageSize); //오름차순
        pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id"); //내림차순


        Page<Board> boardList = repository.findByContentContaining(keyword, pageable);
        return boardList;
    }
}
