package com.LostakTodo.lostakTodo.API.userApiName;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserApiNameRepository extends JpaRepository<UserApiName, Long> {
    
    // User id 값으로 api 테이블 찾기
    Optional<UserApiName> findAllByUserId(Long user_id);
}
