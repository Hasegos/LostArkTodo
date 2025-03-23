package com.LostakTodo.lostakTodo.API.apiService;

import com.LostakTodo.lostakTodo.MemberShip.userData.User;
import com.LostakTodo.lostakTodo.MemberShip.userData.UserRepository;
import com.LostakTodo.lostakTodo.API.userApiName.UserApiName;
import com.LostakTodo.lostakTodo.API.userApiName.UserApiNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class lostArkApiService {

    private final UserRepository userRepository;
    private final UserApiNameRepository userApiNameRepository;

    // DB에 저장된 user 의 저장된 api 키와 닉네임정보찾기
    public Optional<UserApiName> getUserApikeyNickname(Authentication auth){

        // 유저가 로그인한 정보의 이메일 정보로 유저 정보 찾기
        Optional<User> userLine = userRepository.findAllByUserEmail(auth.getName());
        // 이후에 유저 정보중에 id를 이용해서 api, 닉네임 정보 저장 테이블 찾기
        Optional<UserApiName> userInformation = userApiNameRepository.findAllByUserId(userLine.get().getId());

        return userInformation;
    }
}
