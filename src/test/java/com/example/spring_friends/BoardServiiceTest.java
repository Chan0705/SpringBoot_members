package com.example.spring_friends;

import com.example.spring_friends.dto.BoardDTO;
import com.example.spring_friends.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiiceTest {

        @Autowired  //객체 생성(service)
        private BoardService service;

        @Test
        void testInsertData() {
            for(int i = 1; i <= 123; i++) {
                BoardDTO dto = new BoardDTO();
                dto.setTitle("테스트 제목 " + i);
                dto.setContent("테스트 내용 " + i);
                dto.setWriter("tester");

                service.save(dto);
            }
        }
}
