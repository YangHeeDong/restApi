package com.example.restApi.domain.member.service;

import com.example.restApi.domain.member.controller.ApiV1MemberController;
import com.example.restApi.domain.member.entity.Member;
import com.example.restApi.domain.member.repository.MemberRepository;
import com.example.restApi.global.exception.GlobalException;
import com.example.restApi.global.jwt.JwtProvider;
import com.example.restApi.global.rsData.RsData;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    }

    @Transactional
    public RsData<AuthAndMakeTokensRes> authAndMakeTokens(ApiV1MemberController.LoginReq loginReq) {

        Member member = memberRepository.findByUsername(loginReq.username).orElseThrow(() -> new GlobalException("F-1","존재하지 않는 회원"));


        if(!passwordEncoder.matches(loginReq.getPassword(), member.getPassword())){
            throw new GlobalException("F-2","비밀번호가 일치하지 않습니다.");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id",member.getId());
        claims.put("username",member.getUsername());

        String accessToken = jwtProvider.genToken(claims,60*5);

        return RsData.of("S-1","로그인 성공",new AuthAndMakeTokensRes(member,accessToken));

    }
}
