package com.LostakTodo.lostakTodo.MemberShip.userData;

import com.LostakTodo.lostakTodo.API.userApiName.UserApiName;
import com.LostakTodo.lostakTodo.MemberShip.customDomain.allowedDomainEmail;
import com.LostakTodo.lostakTodo.MemberShip.customPassword.allowedPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "user_Data")
@Setter
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 1씩 증가
    private Long id;
    
    // 커스텀 어노테이션 사용해야하는 이유
    /*
    @Email(message = "올바른 이메일 형식이 아닙니다") // 이메일 검증 이게 @ 치고 뒤에 아무문자와도 문제를 안잡아줌
    그래서 커스텀 해야됨
     */

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @allowedDomainEmail(domains = {"naver.com" , "daum.net", "gmail.com"}, // <- 해당 이메일 주소를 배열로 받는 커스텀 어노테이션 사용
            message = "올바른 이메일 형식이 아닙니다.") // 만약 이메일이 더필요하면 작성
    private String userEmail; //유저 이메일

    // @Transient 를 사용해서 JPA 에서 직접적으로 서버에 관리 안하게끔 설정 즉, 입력값만 가져와서 확인해보고
    // 실질적인 데이터는 hashedPassword 에 저장

    @Transient
    @allowedPassword(message = "비밀번호는 8~20자리의 영문자와 숫자 조합이어야 합니다. ") // 커스텀 어노테이션 사용
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String rawPassword; // 입력된 원본 비번

    private String hashedPassword; // 해싱된 비번

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserApiName> apiNames = new ArrayList<>();
}
