<h1 align="center">LOA Check</h1>

<h1>목차</h1>

1. [LOA Check 소개](#loa-check-소개)
2. [팀원 소개](#팀원-소개)
3. [개발 기간 및 개발 환경](#개발-기간-및-개발-환경)
4. [주요 기능](#주요-기능)
5. [서비스 기능 소개](#서비스-기능-소개)

## LOA Check 소개

<p align="center">
<img src="https://github.com/user-attachments/assets/74171823-4db1-4a11-8fd2-6021ea36ea0c">
</p>

<h3>로스트아크 유저를 위한 캐릭터 검색 서비스</h3>

>로스트아크 유저들이 본인의 캐릭터 정보를 검색해보고 찾아볼 수 있는 사이트입니다.

## 팀원 소개

<table align="center">
    <tr>
        <td>
        <a href="https://github.com/Hasegos" target="_blank">
        <img src="https://avatars.githubusercontent.com/u/93961708?s=400&v=4" width=150 alt="최수호 프로필">
        </td>    
    </tr>
</table>

### 역할

- API 구현
    - 회원가입 및 로그인 기능 구현
    - 해당 캐릭터 정보 출력

<br><br>

## 개발 기간 및 개발 환경

### 개발 기간

??

### 개발 환경

#### 주요 기술

Backend

```
- Java
- Java SpringFrameWork
```

Frontend
```
- JavaScript
- HTML5
- CSS
```

CI/CD
```
- AWS EC2
```
DB
```
H2 DataBase
```

#### 전체적인 흐름

![Image](https://github.com/user-attachments/assets/76dcbe31-5b8d-48c3-81d6-948ab336f581)

## 주요 기능

<h3>User</h3>

1. Custom Authorities

+ 해당 프로젝트에서 User에 대한 JWT 토근을 활용해서 사용자 정의 권한 클래스를 구현했습니다.

```java
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
    
    private SecretKey key; // jwt에 쓸 랜덤 키    
    
    @Value("${jwt.secret-Key}") // 안전을 위해 application에 등록후 사용
    public void setKey(String secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // JWT 만들어주는 함수
    public  String createToken(Authentication auth){        

        // 유저에대한 정보를 얻음 그 정보를 CustomUser 를 통해 사용할 정보만 추출
        var user = (customUser) auth.getPrincipal();        
        var authorities = auth.getAuthorities().stream()
                .map(a -> a.getAuthority()).collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .claim("userEmail" , user.getUsername())
                .claim("authorities",authorities) 
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 1000 * 15)) 
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }
    
     // Claim 에 추가된 유저정보 즉, JWT 에있는 정보를 까주는 함수
    public Claims extractToken(String token){

        try {            
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
```

## 서비스 기능 소개

### 1.회원가입 및 로그인

```
로그인, 회원 가입을 통해 메인 화면으로 이동할 수 있습니다.
```

![Image](https://github.com/user-attachments/assets/a94ccb64-54d4-4ac2-906d-5932c4dd047a)

![Image](https://github.com/user-attachments/assets/fdeaf2b0-abde-4a84-8049-d72c57dda58b)

### 2.API 등록

```
공식 로스트아크 API키를 등록하고 이후 검색기능을 사용할 수 있습니다.
```

![Image](https://github.com/user-attachments/assets/a1fe8c54-61bd-43a8-907b-cfd93363fd5c)

### 3.캐릭터 정보 출력

```
정상적인 닉네임과 올바른 API키를 통해 캐릭터 정보를 찾을 수 있습니다.
```

![Image](https://github.com/user-attachments/assets/1b6f217f-ffcb-4b6d-baff-01687fc914fb)