package com.example.spring_friends.controller;


import com.example.spring_friends.dto.MemberDTO;
import com.example.spring_friends.entity.Member;
import com.example.spring_friends.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@AllArgsConstructor
@Slf4j
@RequestMapping("/members") // 1차 URL
@Controller
public class MemberController {

    // 서비스 인스턴스 생성
    private MemberService service;



    //회원 가입 페이지
    @GetMapping("/join")
    public String joinForm(){
        return "member/join";
    }

    //회원가입 처리
    // @ModelAttribute : 클라이언트가 보낸 요청 데이터를 자바 객체에 자동으로 매핑해주는 애노테이션
    // ㄴ 주로 컨트롤러 메서드의 파라미터나 리턴값에 붙여서 사용.
    @PostMapping("/join")
    public String join(@ModelAttribute MemberDTO memberDTO){

        log.info("member:" + memberDTO);
        service.save(memberDTO); // 서비스에 존재하는 save 호출

        return "redirect:/";
    }

    // 회원 목록
    @GetMapping
    public String getMemberList(Model model){

        // 서비스의 findAll 호출
        // Member import 시, entity Member import
        List<Member> memberList = service.findAll();

        // 모델로 저장해서 송신
        model.addAttribute("memberList", memberList);
        return "member/list"; // member/list.html
    }

    // 회원 상세정보 이동 - 1명 보기
    @GetMapping("/{id}")
    public String getMember(@PathVariable Long id, Model model){

        Member member = service.findById(id);
        model.addAttribute("member", member); // 회원보내기

        return "member/info";
    }

    // 로그인
    @GetMapping("/login")
    public String loinForm(){

        return "member/login";
    }

    // 로그인 처리
    // @RequestParam : HTTP 요청 파라미터를 컨트롤러 메서드의 인자로 바인딩할 때 사용하는 어노테이션
    @PostMapping("/login")
    public String loginForm(@RequestParam String email, @RequestParam String pw
    , HttpSession session, RedirectAttributes ra){

        try{
            
            // 입력정보로 로그인 비교 - 로그인 가능 여부 체크
            MemberDTO dto = service.login(email, pw);
            
            // 로그인 성공시 - 세션발급
            session.setAttribute("loginEmail", dto.getEmail());
            return "redirect:/";
        } catch (Exception e){
            // 로그인 실패 시 에러 메세지 출력
            // RedirectAttributes는 redirect 상태에서 에러메세지 출력
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/members/login";
        }

//        return "/home";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session){

        // 세션 삭제
        session.invalidate();
        return "redirect:/";

    }

}
