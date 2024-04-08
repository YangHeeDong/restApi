package com.example.restApi.domain.member.controller;

import com.example.restApi.domain.member.dto.MemberDto;
import com.example.restApi.domain.member.entity.Member;
import com.example.restApi.domain.member.service.MemberService;
import com.example.restApi.global.rq.Rq;
import com.example.restApi.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {

    private final MemberService memberService;
    private final Rq rq;

    @Getter
    public static class LoginReq {

        @NotBlank
        public String username;

        @NotBlank
        public String password;

    }

    @AllArgsConstructor
    @Getter
    public static class LoginRes {
        private MemberDto memberDto;
    }

    @PostMapping("/login")
    public RsData<LoginRes> login (@Valid @RequestBody LoginReq loginReq, BindingResult br) {

        RsData<MemberService.AuthAndMakeTokensRes> result= memberService.authAndMakeTokens(loginReq);

        if(result.getIsFail()){
           return RsData.of(
                   result.getResultCode(),
                   result.getMsg());
        }

        // 토큰을 쿠키에 등록

        rq.setCrossDomainCooke("accessToken", result.getData().getAccessToken());

        return RsData.of(
                result.getResultCode(),
                result.getMsg(),
                new LoginRes(
                        new MemberDto(result.getData().getMember())
                )
        );
    }

}
