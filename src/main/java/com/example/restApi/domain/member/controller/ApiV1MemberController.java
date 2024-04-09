package com.example.restApi.domain.member.controller;

import com.example.restApi.domain.member.dto.MemberDto;
import com.example.restApi.domain.member.entity.Member;
import com.example.restApi.domain.member.service.MemberService;
import com.example.restApi.global.exception.GlobalException;
import com.example.restApi.global.rq.Rq;
import com.example.restApi.global.rsData.RsData;
import com.example.restApi.global.security.SecurityUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
        
        if(br.hasErrors()){
            throw new GlobalException("F-1","유효성 에러");
        }
        
        RsData<MemberService.AuthAndMakeTokensRes> result= memberService.authAndMakeTokens(loginReq);

        if(result.getIsFail()){
           return RsData.of(
                   result.getResultCode(),
                   result.getMsg());
        }

        // 토큰을 쿠키에 등록
        rq.setCrossDomainCookie("accessToken", result.getData().getAccessToken());
        rq.setCrossDomainCookie("refreshToken", result.getData().getRefreshToken());

        SecurityUser securityUser = memberService.getUserFromAccessToken(result.getData().getAccessToken());
        rq.setLogin(securityUser);

        return RsData.of(
                result.getResultCode(),
                result.getMsg(),
                new LoginRes(
                        new MemberDto(result.getData().getMember())
                )
        );
    }

    @PostMapping("/logout")
    public RsData<String> logout () {

        // 토큰을 쿠키에 등록
        rq.removeCrossDomainCookie("accessToken");
        rq.removeCrossDomainCookie("refreshToken");

        SecurityContextHolder.setContext(null);

        return RsData.of(
                "S-1",
                "로그아웃 되었습니다."
        );
    }

    @AllArgsConstructor
    @Getter
    public static class MeRes{
        private final MemberDto memberDto;
    }

    @GetMapping("/me")
    public RsData<MeRes> me () {

        Member member = rq.getMember();

        return RsData.of(
                "S-2",
                "조회성공",
                new MeRes(new MemberDto(member))
        );
    }

}
