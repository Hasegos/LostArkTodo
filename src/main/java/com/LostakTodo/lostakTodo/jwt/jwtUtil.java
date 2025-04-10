package com.LostakTodo.lostakTodo.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Component
// JWT 생성 / JWT 까주는 함수 포함된  클래스
public class jwtUtil {

    // application.properties 에 있는 key 를 static있는 키가 주입이안됨.
    private SecretKey key; // jwt에 쓸 랜덤 키
    
    // 여기서 @Value 는 인스턴스 방식이라서 static 기반제거후 리팩토링
    @Value("${jwt.secret-Key}") // 안전을 위해 application에 등록후 사용
    public void setKey(String secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
    // JWT 만들어주는 함수
    public  String createToken(Authentication auth){
        // .claim(이름, 값) 으로 JWT 에 데이터 추가 가능

        // 유저에대한 정보를 얻음 그 정보를 CustomUser 를 통해 사용할 정보만 추출
        var user = (customUser) auth.getPrincipal();
        // 권한을 맵 자료로 받고 ,  를 기준으로 나눠서 등록
        var authorities = auth.getAuthorities().stream()
                .map(a -> a.getAuthority()).collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .claim("userEmail" , user.getUsername())
                .claim("authorities",authorities) // 문자만 입력가능
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 1000 * 15)) // jwt 생존시간 ms으로 계산함
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }
    
     // Claim 에 추가된 유저정보 즉, JWT 에있는 정보를 까주는 함수
    public Claims extractToken(String token){

        try {
            // Claims로
            Claims claims = Jwts.parser()
                    .verifyWith(key) // SecretKey 를 이용해 JWT 서명 검증
                    .build()// 파서 빌드 완료
                    .parseSignedClaims(token) // 서명된 JWT(JWS) 토큰을 파싱 및 검증
                    .getPayload(); // JWT 의 페이로드(Claims)를 추출
            return claims;
        }catch (Exception e){
            System.out.println("JWT 검증 실패: " + e.getMessage());
            throw new RuntimeException("JWT 검증 실패", e);
        }
    }
}
