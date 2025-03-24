package com.LostakTodo.lostakTodo.API;

import com.LostakTodo.lostakTodo.API.apiService.lostArkApiService;
import com.LostakTodo.lostakTodo.API.userApiName.UserApiName;
import com.LostakTodo.lostakTodo.API.userApiName.UserApiNameRepository;
import com.LostakTodo.lostakTodo.API.userApiData.lostArkAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
@RequiredArgsConstructor
public class LostArkAPIController {

    private final lostArkAPI lostArkAPI;
    private final UserApiNameRepository userApiNameRepository;
    private final lostArkApiService lostArkApiService;

    // api 키 등록 페이지
    @GetMapping("/api")
    String API(){
        return "api/api";
    }

    // 특정 캐릭터 정보 (이름, 레벨, 이미지, 클래스 이름)
    @GetMapping("/home/userProfiles")
    public ResponseEntity<?> getUserProfiles(@RequestParam String playerId, Authentication auth) {
        UserApiName userApiInformation = lostArkApiService.getUserApikeyNickname(auth).get();

        String apiKey = userApiInformation.getApiKey();

        try {
            String jsonString = lostArkAPI.getUserProfiles(playerId, apiKey.trim());
            return ResponseEntity.ok(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "해당 유저정보를 가져올 수 없습니다."));
        }
    }

    // 특정 캐릭터 장비 정보 (아이템 정보)
    @GetMapping("/home/userEquipment")
    public ResponseEntity<?> getUserEquipment(@RequestParam String playerId, Authentication auth){
        UserApiName userApiInformation = lostArkApiService.getUserApikeyNickname(auth).get();

        String apiKey = userApiInformation.getApiKey();

        try {
            String jsonString = lostArkAPI.getUserEquipment(playerId,apiKey.trim());
            return ResponseEntity.ok(jsonString);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "해당 유저정보를 가져올 수 없습니다."));
        }
    }

    @GetMapping("/home/userEngravings")
    public ResponseEntity<?> getUserEngravings(@RequestParam String playerId, Authentication auth){
        UserApiName userApiInformation = lostArkApiService.getUserApikeyNickname(auth).get();

        String apikey = userApiInformation.getApiKey();

        try {
            String jsonString = lostArkAPI.getUserEngravings(playerId,apikey.trim());
            return ResponseEntity.ok(jsonString);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error","해당 유저정보를 가져올 수 없습니다."));
        }
    }

    // API 키 등록시 DB에 등록
    @PostMapping("/DBApi") // 캐릭터 정보 추가 할떄 발생
    String API_Get(@RequestParam String API_key, String playerId, Authentication auth){

        // 존재하지않을 경우
        if(!lostArkApiService.getUserApikeyNickname(auth).isPresent()){
            lostArkAPI.setApiKey(API_key, auth, playerId);
            return "redirect:/home";
        }
        // 이미 존재할경우 업데이트
        if(lostArkApiService.getUserApikeyNickname(auth).isPresent()){
            UserApiName remove = lostArkApiService.getUserApikeyNickname(auth).get();
            userApiNameRepository.delete(remove);
        }
        lostArkAPI.setApiKey(API_key, auth, playerId);

        return "redirect:/home";
    }
}
