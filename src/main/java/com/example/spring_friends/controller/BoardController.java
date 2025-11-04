package com.example.spring_friends.controller;


import com.example.spring_friends.dto.BoardDTO;
import com.example.spring_friends.entity.Board;
import com.example.spring_friends.service.BoardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Controller // bean 등록
@RequestMapping("/boards")
public class BoardController {

    //
    private final BoardService service;

    // 자게
    //게시글 목록(페이지 처리 및 검색)
    //http://localhost:8080/boards/pages?page=1 (기본 페이지)
    @GetMapping("/pages")
    public String getBoardPages(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword,
            // required = false : 키워드가 없어도 예외가 발생하지 않도록 추가
            @PageableDefault(page=1) Pageable pageable,
                                Model model) {

        Page<Board> boardList = null;
        if(keyword == null) {
            boardList = service.findAll(pageable); // 일반페이지 처리
        }else if (keyword != null && type.equals("title")){
            boardList = service.findByTitleContaining(keyword, pageable); // 제목검색어 처리
        }else if (keyword != null && type.equals("content")){
            boardList = service.findByContentContaining(keyword, pageable); // 제목검색어 처리
        }

        //하단의 페이지 블럭
        int blockLimit = 10; //1 2 3 ... 10 (10페이지까지 보이기)
        //페이지 블럭의 시작 번호 -  1, 11, 21
        //예)페이지 번호 - 13,  13/10=1.3-> 2(올림)-1 * 10 + 1 => 11 (11 ~ 20 블럭)
        int startPage =
                ((int)Math.ceil((double)pageable.getPageNumber() / blockLimit) - 1)
                        * blockLimit + 1;

        //페이지 블럭의 끝번호 - 10, 20, 30
        //int endPage = startPage + blockLimit - 1  (
        //
		/*int endPage = (startPage + blockLimit - 1) > boardList.getTotalPages() ?
				boardList.getTotalPages() : (startPage + blockLimit - 1);*/

        int endPage = Math.min(startPage + blockLimit - 1, boardList.getTotalPages());

        log.info("startPage: " + startPage);
        log.info("pageable.getPageNumber(): " + pageable.getPageNumber());
        log.info("endPage: " + endPage);
        log.info("boardList.getTotalPages(): " + boardList.getTotalPages());


        //모델 보내기
        model.addAttribute("boardList", boardList); //리스트 보내기
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("kw", keyword); // 검색어 보내기
        model.addAttribute("type", type); // type: 검색유형 보내기


        return "boards/pages";
    }

    // 글쓰기 페이지
    @GetMapping("/write") // 2차 url
    public String writeForm(BoardDTO dto){
        return "boards/write"; // boards/write.html
    }

    // 글쓰기 처리 - post
    @PostMapping("/write")
    public String write(@Valid BoardDTO dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "boards/write"; // 에러 발생 시 돌려보낼 주소
        }
        service.save(dto);
        return "redirect:/boards/pages";
    }

    // 상세정보
    @GetMapping("/{id}")
    public String getBoard(@PathVariable Long id,
                           @PageableDefault(page=1) Pageable pageable,
                           Model model)
    {try {
            // 조회수 증가
            service.updateHits(id);

            // 상세정보 보기
            Board board = service.findById(id);
            model.addAttribute("board", board);
            model.addAttribute("page", pageable.getPageNumber());
            return "boards/info";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "error/errorPage";
        }
    }

    //글 삭제
    @GetMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/boards";
    }

    //글 수정 페이지
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id,
                             Model model) {
        Board board = service.findById(id); //해당 게시글 가져옴
        model.addAttribute("board", board); //수정페이지로 보냄
        return "board/update";
    }

    //글 수정 처리
    @PostMapping("/update")
    public String updateBoard(BoardDTO dto) {
        service.update(dto);
        return "redirect:/boards/" + dto.getId();
    }

}
