package com.LostakTodo.lostakTodo.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class customUser extends User {

    public String api_key;

    // 유저에대한 정보를 얻고싶을 걸 커스텀해버리면됨
    public customUser(String username, String password, // 유저에대한 권한, 이메일, 비번을관리를 위해 사용
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
