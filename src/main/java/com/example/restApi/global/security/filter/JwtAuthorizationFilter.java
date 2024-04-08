package com.example.restApi.global.security.filter;

import com.example.restApi.domain.member.entity.Member;
import com.example.restApi.domain.member.service.MemberService;
import com.example.restApi.global.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter 는 모든 서블릿에서 일관된 요청을 처리하기 위해 만들어진 필터이다.
// 이 추상 클래스를 구현한 필터는 사용자의 한번에 요청 당 딱 한번만 실행되는 필터를 만들 수 있다.
// 헤더에 토큰이 있든 없든 그냥 해버리나?
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        // 헤더에서 Authorization 값을 가져온다.
        String bearerToken = request.getHeader("Authorization");

        // bearerToken > JWT, OAuth 같은 토큰을 사용한다.
        if (bearerToken != null) {
            // 토큰만 뽑아오기 ( Bearer 토큰임을 나타낸 앞 "Bearer " 제거 )
            String token = bearerToken.substring("Bearer ".length());

            // 토큰이 유효하다면
            if (jwtProvider.verify(token)) {

                // 토큰에서부터 id 정보를 받아와
                Map<String, Object> claims = jwtProvider.getClaims(token);
                long id = (int)claims.get("id");

                // 회원을 찾은 후
                Member member = memberService.findById(id).orElseThrow();

                // 해당 회원으로 로그인 처리
                forceAuthentication(member);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 강제로 로그인 처리하는 메소드
    private void forceAuthentication(Member member) {
        User user = new User(member.getUsername(), member.getPassword(), member.getAuthorities());

        // 스프링 시큐리티 객체에 저장할 authentication 객체를 생성
        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        user,
                        null,
                        member.getAuthorities()
                );

        // 스프링 시큐리티 내에 우리가 만든 authentication 객체를 저장할 context 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // context에 authentication 객체를 저장
        context.setAuthentication(authentication);
        // 스프링 시큐리티에 context를 등록
        SecurityContextHolder.setContext(context);
    }
}
