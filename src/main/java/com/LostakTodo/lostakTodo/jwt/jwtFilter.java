package com.LostakTodo.lostakTodo.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;


// jwt가 유효한지 검사하고 이후에 문제없으면 정보를 넣어줌
@Service
@Component
public class jwtFilter extends OncePerRequestFilter {

    // 인스턴스 기반 jwtUtil DI(의존성) 을 추가
    private final jwtUtil jwtUtil;

    public jwtFilter(jwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
    throws ServletException, IOException {

        System.out.println("JWTFilter 시작");

        // 1. jwt 이름의 쿠키가 있으면 꺼내보기
        Cookie[] cookies =request.getCookies();
        if(cookies == null){
            filterChain.doFilter(request,response); // 다음 필터 실행
            return;
        }
        // 반복문을 통해 jwt 라는 이름을 가진 쿠키를 저장
        var jwtCookie = "";
        for (int i = 0; i < cookies.length; i++){
            if(cookies[i].getName().equals("jwt")){
                jwtCookie = cookies[i].getValue();
            }
        }

        // 2.유효기간 , 위조 여부 등 확인해야됨
        Claims claims;

        try {
            claims = jwtUtil.extractToken(jwtCookie);
        }catch (Exception e){
            System.out.println("JWT 검증 실패: " + e.getMessage());
            filterChain.doFilter(request,response); // 다음 필터 실행
            return;
        }
        // list 자료형이라서하나씩 , 기준으로 짤라 배열형태로
        var arr = claims.get("authorities").toString().split(",");;

        
        var authorities = Arrays.stream(arr)
                .map(a -> new SimpleGrantedAuthority(a)).toList();

        var customUer = new customUser(
                claims.get("userEmail").toString(),
                "none", // 비밀번호는 민감한 정보라 가져오지않기
                authorities
        );

        // 문제 없으면 auth 변수에 유저 정보를 넣어줌
        var authToken = new UsernamePasswordAuthenticationToken(
                customUer, null , authorities // 여기에 더 넣고싶으면 CustomUser에넣어도됨
                // auth 값으로 customUser 값 , null , 권한값 다넣어야 작동가능
        );
        // auth변수에 detail 추가하는 부분
        authToken.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request)
        );

        // 최신화 정보를 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);

        System.out.println("JWTFilter 종료");
    }
}
