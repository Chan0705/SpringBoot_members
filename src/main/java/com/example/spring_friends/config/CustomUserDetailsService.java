package com.example.spring_friends.config;


import com.example.spring_friends.entity.Member;
import com.example.spring_friends.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // = new , 인스턴스 객체생성과 동일
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepo;

    @Override // Email 검색으로 사용자 가져오기 override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepo.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(member);
    }
}
