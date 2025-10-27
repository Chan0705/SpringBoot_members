package com.example.spring_friends.service;


import com.example.spring_friends.dto.MemberDTO;
import com.example.spring_friends.entity.Member;
import com.example.spring_friends.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// 스프링에 빈(bean) 등록
// DAO(=Repository)에서 데이터 받아옴

@AllArgsConstructor // 모든 매개변수를 가진 생성자
@Service
public class MemberService {

    
    // 인스턴스 객체 생성 방식 - 생성자 주입 방식
    private MemberRepository repository;

//    public MemberService(MemberRepository repository){
//        this.repository = repository;
//    } 와 동일

    // 회원가입 추가
    public void save(MemberDTO dto){

        // dto를 entity로 변환 메서드 호출 - member에서 작성
        Member member = Member.toSaveEntity(dto);

        repository.save(member);
    }

    // 회원목록
    public List<Member> findAll(){

        return repository.findAll();

    }

    // 회원정보
    public Member findById(Long id){
        Member member = repository.findById(id).get();

        return member;
    }

    // 로그인 처리
    public MemberDTO login(String email, String pw){
        // DB의 정보와 Form에 입력된 이메일 정보를 비교
        Member member = repository.findByEmail(email) // repo는 entity에서 꺼내는 것
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));

        // pw 정보 비교
        if(!member.getPw().equals(pw)){
            throw new IllegalArgumentException("비밀번호 불일치");
        }


        // entity를 dto에 저장
        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setEmail(member.getEmail());
        dto.setName(dto.getName());
        dto.setGender(dto.getGender());

        return dto;
    }

}
