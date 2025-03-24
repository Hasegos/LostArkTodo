package com.LostakTodo.lostakTodo.API.userApiData;


import com.LostakTodo.lostakTodo.MemberShip.userData.User;
import com.LostakTodo.lostakTodo.MemberShip.userData.UserRepository;
import com.LostakTodo.lostakTodo.API.userApiName.UserApiName;
import com.LostakTodo.lostakTodo.API.userApiName.UserApiNameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

// 로스트아크 API 키 가져오기
@Service
@RequiredArgsConstructor

public class lostArkAPI {

    private final UserApiNameRepository userApiNameRepository;
    private final UserRepository userRepository;

    // application 에 저장된 설정값을 하드 코딩없이 가져올수있게 설정해주는 어노테이션
    @Value("${game.api.url}")
    private String apiUrl;

    // HTTP 메서드(GET, POST, PUT, DELETE 등) 처리할수있게 해줌
    private final RestTemplate restTemplate = new RestTemplate();

    // api 키와 플레리어 닉네임 저장
    public void setApiKey(String apiKey, Authentication auth, String playerId){

        try {
            // User 에 있는 정보를 찾기
            Optional<User> result = userRepository.findAllByUserEmail(auth.getName());
            User user = result.get();

            // Api 테이블에 user 정보를 외래참조로 저장 후에 api 키와 닉네임 저장
            UserApiName userApiName = new UserApiName();

            // 로아 공식 API 키 공백 자체없애고 저장
            String ApiKey = apiKey.replaceAll("\\s", "").trim();


            userApiName.setUser(user);
            userApiName.setApiKey(ApiKey);
            userApiName.setUserName(playerId);

            userApiNameRepository.save(userApiName);
        }catch (Exception e){
            System.out.println("에러 메세지" + e.getMessage());
        }
    }

    // 해당 유저의 정보 (이름, 이미지, 아이템 레벨 , 직업)
    public String getUserProfiles(String playerId , String apiKey){

        // UriComponentsBuilder.fromHttpUrl 를사용해서 기본적인 URL에 동적으로 경로를 추가
        String userProfilesUrl = UriComponentsBuilder.fromHttpUrl(apiUrl +
                        "/armories/characters/" + playerId + "/profiles")
                .toUriString();

        // 인증정보나 기타 요청에 필요한 정보를 전달하기위해서
        // HTTP 헤더는 클라이언트와 서버 간의 요청과 응답에 추가적인 정보 제공(특정정보를 헤더에 포함가능)
        // 로스트아크 API 특성상 Authorization 권한과 Bearer 토근에 api키를 같이넣어줘야함
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> userProfilesApi = null;

        try {
            // 해당 url 과 Get 요청, 헤더설정
            userProfilesApi = restTemplate.exchange(userProfilesUrl , HttpMethod.GET , entity , String.class);
            // Json 데이터 그대로 넘겨주기
            return userProfilesApi.getBody();
        }
        catch (Exception e){
            e.printStackTrace();
            return "해당 유저 정보가없습니다.";
        }
    }

    // 해당 유저의 정보 (아이템 정보)
    public String getUserEquipment(@RequestParam String playerId , String apiKey){

        String userProfilesUrl = UriComponentsBuilder.fromHttpUrl(apiUrl +
                        "/armories/characters/" + playerId + "/equipment")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> userProfilesApi = null;

        try {
            userProfilesApi = restTemplate.exchange(userProfilesUrl , HttpMethod.GET , entity , String.class);

            return userProfilesApi.getBody();
        }
        catch (Exception e){
            e.printStackTrace();
            return "해당 유저 정보가없습니다.";
        }
    }

    // 해당 유저의 정보 (각인 정보)
    public String getUserEngravings(@RequestParam String playerId, String apiKey){

        String userProfilesUrl = UriComponentsBuilder.fromHttpUrl(apiUrl +
               "/armories/characters/" + playerId + "/engravings")
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> userProfilesApi = null;

        try {
            userProfilesApi = restTemplate.exchange(userProfilesUrl, HttpMethod.GET , entity , String.class);

            return  userProfilesApi.getBody();
        }
        catch (Exception e){
            e.printStackTrace();
            return "해당 유저 정보가 없습니다.";
        }
    }
}
