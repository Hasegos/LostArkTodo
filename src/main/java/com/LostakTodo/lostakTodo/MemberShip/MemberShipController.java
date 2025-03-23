package com.LostakTodo.lostakTodo.MemberShip;

import com.LostakTodo.lostakTodo.MemberShip.service.memberShipErrorDTO;

import com.LostakTodo.lostakTodo.MemberShip.userData.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller 
@RequiredArgsConstructor // 레포지토리 등록떄문에
public class MemberShipController {

    private final com.LostakTodo.lostakTodo.MemberShip.service.memberShipService memberShipService; // User 레포지토리 등록


    @GetMapping("/membership")
    // 회원 가입페이지
    String MemberShip(){
        return "membership/membership";
    }

    @PostMapping("/membership")
    String MemberShip_Post(@RequestParam
                           String rawPassword,
                           String password_Again,@Valid User userData,
                           BindingResult bindingResult, Model model) {
        try {
            // 서비스 구역에 검증 (코드가 비대해지는거 막기위해서)
            memberShipErrorDTO memberShipErrorDTO = memberShipService.memberShipValid(
                    rawPassword,
                    password_Again, bindingResult);

            // 검증이 실패시
            if (!memberShipErrorDTO.isValid()) {
                model.addAttribute("error" + memberShipErrorDTO.getFieldName(), memberShipErrorDTO.getErrorMessage());
                return "membership/membership";
            }
            
            // 검증 성공시 저장
            memberShipService.membership(userData.getUserEmail(), rawPassword, userData);
            return "home/home"; // 정상적으로 저장시 메인페이지 이동

        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

}
