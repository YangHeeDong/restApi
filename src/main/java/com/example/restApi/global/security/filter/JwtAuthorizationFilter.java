package com.example.restApi.global.security.filter;

import com.example.restApi.domain.member.service.MemberService;
import com.example.restApi.global.jwt.JwtProvider;
import com.example.restApi.global.rq.Rq;
import com.example.restApi.global.rsData.RsData;
import com.example.restApi.global.security.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter 는 모든 서블릿에서 일관된 요청을 처리하기 위해 만들어진 필터이다.
// 이 추상 클래스를 구현한 필터는 사용자의 한번에 요청 당 딱 한번만 실행되는 필터를 만들 수 있다.
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final Rq rq;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        if(request.getRequestURI().equals("/api/v1/members/login") || request.getRequestURI().equals("/api/v1/members/logout")){
            filterChain.doFilter(request,response);
            return;
        }

        String accessToken = rq.getCookieValue("accessToken", "");

        if(!accessToken.isBlank()){

            if (!memberService.validateToken(accessToken)) {
                String refreshToken = rq.getCookieValue("refreshToken", "");

                RsData<String> rs = memberService.refreshAccessToken(refreshToken);
                accessToken = rs.getData();
                rq.setCrossDomainCookie("accessToken", accessToken);
            }

            SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
            rq.setLogin(securityUser);
        }

        filterChain.doFilter(request, response);

    }

    // 아래 주석은 frontEnd에서 header에 accessToken을 담아 보낼때 사용하는 코드임
    // 현재 httpOnly일 때 frontEnd에서 cookie에 접근할 수 없어 토큰 값을 못가져옴
    // 고로 httpOnly가 아닐 때 사용가능한 코드임 > access 토큰 받는 부분만 쿠키에서 받아오면 쓸 수 있음
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
//
//        // 헤더에서 Authorization 값을 가져온다.
//        String bearerToken = request.getHeader("Authorization");
//
//        // bearerToken > JWT, OAuth 같은 토큰을 사용한다.
//        if (bearerToken != null) {
//            // 토큰만 뽑아오기 ( Bearer 토큰임을 나타낸 앞 "Bearer " 제거 )
//            String token = bearerToken.substring("Bearer ".length());
//
//            // 토큰이 유효하다면
//            if (jwtProvider.verify(token)) {
//
//                // 토큰에서부터 id 정보를 받아와
//                Map<String, Object> claims = jwtProvider.getClaims(token);
//                long id = (int)claims.get("id");
//
//                // 회원을 찾은 후
//                Member member = memberService.findById(id).orElseThrow();
//
//                // 해당 회원으로 로그인 처리
//                forceAuthentication(member);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    // 강제로 로그인 처리하는 메소드
//    private void forceAuthentication(Member member) {
//        User user = new User(member.getUsername(), member.getPassword(), member.getAuthorities());
//
//        // 스프링 시큐리티 객체에 저장할 authentication 객체를 생성
//        UsernamePasswordAuthenticationToken authentication =
//                UsernamePasswordAuthenticationToken.authenticated(
//                        user,
//                        null,
//                        member.getAuthorities()
//                );
//
//        // 스프링 시큐리티 내에 우리가 만든 authentication 객체를 저장할 context 생성
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        // context에 authentication 객체를 저장
//        context.setAuthentication(authentication);
//        // 스프링 시큐리티에 context를 등록
//        SecurityContextHolder.setContext(context);
//    }
}
