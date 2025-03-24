package com.LostakTodo.lostakTodo.login;

import com.LostakTodo.lostakTodo.jwt.jwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
// 자동 생성자 생성 생성자 생성 X
@RequiredArgsConstructor
public class loginController {

    // jwtUtil DI 의존성 추가
    private final jwtUtil jwtUtil;
    // 로그인을 자동으로 시켜주기위해서
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 로그인페이지
    @GetMapping("/login")
    String Login(){
        return "login/login";
    }

    @PostMapping("/loginJWT")
    ResponseEntity<String> Login_Post(@RequestBody Map<String,String> data,
                                      HttpServletResponse response)  {
        
        // Json 형태로 얻은 데이터를 토큰형태로 저장
        var authToken =  new UsernamePasswordAuthenticationToken(
                data.get("userEmail"), data.get("password")
        );

        // 로그인 이메일 , 비번 넣으면 자동으로해줌 (만약 수동으로 할 경우 유저 정보가 반영 x)
        var auth= authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth); // 최신화 정보 저장

        var jwt = jwtUtil.createToken(SecurityContextHolder.getContext().getAuthentication()); // 최신화정보를 jwt에 넣기


        var cookie = new Cookie("jwt", jwt); // 최신화된 jwt 정보를 쿠키에 저장

        cookie.setMaxAge(100); // 100초
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("Login successful");
    }

}
