package com.LostakTodo.lostakTodo.home;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class homeController {

    @GetMapping("/home")
    String home( HttpServletRequest request,Authentication authentication){

        return "home/home";
    }
}
