package com.example.restApi.domain.article.controller;

import com.example.restApi.domain.member.entity.Member;
import com.example.restApi.domain.member.service.MemberService;
import com.example.restApi.global.exception.GlobalException;
import com.example.restApi.global.jwt.JwtProvider;
import com.example.restApi.global.rsData.RsData;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ApiV1ArticleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("GET /articles/1")
    void test1 () throws Exception {

         Member member = memberService.findById(1L).orElseThrow( () -> {
             return new GlobalException("F-1","존재하지 않는 회원");
         });

        String accessToken = jwtProvider.genToken(member,60*60*50);

        ResultActions ra = mvc.perform(
                                    get("/api/v1/articles/1")
                                    .header("Authorization","Bearer "+accessToken)

                            ).andDo(print());



        ra.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article.id").value(1));
    }

}
