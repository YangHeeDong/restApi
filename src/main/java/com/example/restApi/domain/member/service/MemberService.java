package com.example.restApi.domain.member.service;

import com.example.restApi.domain.member.controller.ApiV1MemberController;
import com.example.restApi.domain.member.entity.Member;
import com.example.restApi.domain.member.repository.MemberRepository;
import com.example.restApi.global.exception.GlobalException;
import com.example.restApi.global.jwt.JwtProvider;
import com.example.restApi.global.rsData.RsData;
import com.example.restApi.global.security.SecurityUser;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public Member join(String username, String password, String email) {

        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

        member = member.toBuilder().refreshToken(jwtProvider.genRefreshToken(member)).build();

        memberRepository.save(member);

        return member;
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @AllArgsConstructor
    @Getter
    public static class AuthAndMakeTokensRes {
        private Member member;
        private String accessToken;
        private String refreshToken;
    }

    @Transactional
    public RsData<AuthAndMakeTokensRes> authAndMakeTokens(ApiV1MemberController.LoginReq loginReq) {

        Member member = memberRepository.findByUsername(loginReq.username).orElseThrow(() -> new GlobalException("F-1","존재하지 않는 회원"));

        if(!passwordEncoder.matches(loginReq.getPassword(), member.getPassword())){
            throw new GlobalException("F-2","비밀번호가 일치하지 않습니다.");
        }

        String refreshToken = member.getRefreshToken();

        String accessToken = jwtProvider.genAccessToken(member);

        return RsData.of("S-1","로그인 성공",new AuthAndMakeTokensRes(member,accessToken,refreshToken));

    }

    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = jwtProvider.getClaims(accessToken);

        long id = (int) payloadBody.get("id");
        String username = (String) payloadBody.get("username");
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new SecurityUser(
                id,
                username,
                "",
                authorities
        );
    }

    public boolean validateToken(String token) {
        return jwtProvider.verify(token);
    }

    public RsData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new GlobalException("400-1", "존재하지 않는 리프레시 토큰입니다."));

        String accessToken = jwtProvider.genAccessToken(member);

        return RsData.of("200-1", "토큰 갱신 성공", accessToken);
    }

}
