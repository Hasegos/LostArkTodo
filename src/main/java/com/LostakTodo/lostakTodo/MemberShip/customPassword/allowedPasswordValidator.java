package com.LostakTodo.lostakTodo.MemberShip.customPassword;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// 어노테이션 사용할려고 했으나 계속 된 데이터 저장때문에 로직을 안에서 작업
public class allowedPasswordValidator implements ConstraintValidator<allowedPassword, String> {

    @Override
    public boolean isValid(String rawPassword, ConstraintValidatorContext constraintValidatorContext) {

        if (rawPassword == null || rawPassword.isBlank()){
            return false; // null / 빈칸 X
        }
        return rawPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$"); // 비밀번호는 8~20자리의 영문자와 숫자 조합
    }
}
