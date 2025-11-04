package com.example.spring_friends.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor // 생성자 주입
@EnableWebSecurity  //웹 보안기능 활성화
@Configuration  //스프링 Bean 인식(클래스로 등록)
public class SecurityConfig {
    
    private final CustomUserDetailsService userDetailsService;

    // DB에서 암호화된 pw 꺼내오기
    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder encoder){

        // 인스턴스 객체 생성
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService); // email 세팅
        authProvider.setPasswordEncoder(encoder); // pw 설정

        return authProvider;

    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/css/**",
                                        "/images/**", "/members/join",
                                        "/boards/pages"
                                ).permitAll() //해당 경로 접근 허용
                                .requestMatchers("/mebers/**").hasAnyRole("USER", "ADMIN")
                                // USER만 가능(hasRole) or USER ADMIN 둘 다 가능(hasAnyRole)
//                        .requestMatchers("/user/**").hasRole("USER") // USER만 가능
                                .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN만 가능
                                .anyRequest().authenticated() //나머지는 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/members/login") // 사용자의 로그인 페이지 호출
                        .loginProcessingUrl("/login") // 로그인 POST 페이지 호출
                        .defaultSuccessUrl("/", true) // 
                        .permitAll() //누구나 접근 가능
                )
                .logout(logout -> logout
                        .logoutRequestMatcher
                                (new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/") // 로그아웃 후 이동할 페이지 설정
                        .invalidateHttpSession(true) // 세션삭제
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                )
                .exceptionHandling // 접근 권한 오류 설정
                (ex -> ex.accessDeniedPage("/noneaccess"));

        return http.build();
    } // SecurityFilterChain 닫기

    // 비밀번호 암호화
    @Bean
    PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder(); // 인스턴스 객채 생성
    }

    // 사용자 계정 생성
    @Bean
    UserDetailsService userDetailsService(){

        UserDetails user = User.builder()
                .username("guest")
                .password(passwordEncoder().encode("1234")) // 비밀번호 암호화
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("1234")) // 비밀번호 암호화
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

}