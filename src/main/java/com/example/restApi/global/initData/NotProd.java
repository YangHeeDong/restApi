package com.example.restApi.global.initData;

import com.example.restApi.domain.article.controller.ApiV1ArticleController;
import com.example.restApi.domain.article.service.ArticleService;
import com.example.restApi.domain.member.entity.Member;
import com.example.restApi.domain.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(ArticleService articleService, MemberService memberService, PasswordEncoder passwordEncoder) {

        String password = passwordEncoder.encode("1234");

        return args -> {

            // 회원 3명 추가
            Member user1 = memberService.join("user1", password, "test@test.com");
            Member user2 = memberService.join("user2", password, "test@test.com");
            Member admin  = memberService.join("admin", password, "admin@test.com");


            // 작성자 회원 추가
            articleService.save(user1,ApiV1ArticleController.ArticleSaveReq.builder().title("제목1").content("내용1").build());
            articleService.save(user1,ApiV1ArticleController.ArticleSaveReq.builder().title("제목2").content("내용2").build());
            articleService.save(user2,ApiV1ArticleController.ArticleSaveReq.builder().title("제목3").content("내용3").build());
            articleService.save(user2,ApiV1ArticleController.ArticleSaveReq.builder().title("제목4").content("내용4").build());
            articleService.save(admin,ApiV1ArticleController.ArticleSaveReq.builder().title("제목5").content("내용5").build());


        };
    }
}