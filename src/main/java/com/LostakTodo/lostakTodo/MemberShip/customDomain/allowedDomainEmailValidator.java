package com.LostakTodo.lostakTodo.MemberShip.customDomain;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import java.util.Arrays;

public class allowedDomainEmailValidator implements ConstraintValidator<allowedDomainEmail, String> {

    private String[] AllowedDomains; // 허용하는 도메인을 여러개받게끔 설정

    @Override
    public void  initialize(allowedDomainEmail constraintAnnotation){
        this.AllowedDomains = constraintAnnotation.domains();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if (value == null || !value.contains("@")) {
            return false;
        }
        // 배열을 스트림으로 분해후 순회하면서 @ + domain 값을 반환
        return Arrays.stream(AllowedDomains).anyMatch(domain -> value.endsWith("@" + domain));
    }
}
