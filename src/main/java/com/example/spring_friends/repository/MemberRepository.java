package com.example.spring_friends.repository;

// repository는 interface

import com.example.spring_friends.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository <Member, Long>{

    // 지원 메서드 - save(), findAll(전체 검색), findById(1건 검색), deleteById(1건 삭제) ~

    public Optional<Member> findByEmail(String email);

}
