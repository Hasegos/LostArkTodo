const characterInformation = document.getElementById("characterInformation");


// 페이지 로딩후 js 실행

document.addEventListener("DOMContentLoaded", () => {




     // (캐릭터 서버, 이름, 레벨, 직업 이렇게만)
    function userSiblings() {
        // 대표 캐릭터
        const repCharacter = document.getElementById("repCharacter").innerText;
        // 대표 캐릭터 서버
        let repCharacterServer;



        // 대표 캐릭터 서버 찾기
        getJsonElementById("characterSiblings").forEach((item) =>{
            if(item.CharacterName == repCharacter){
               repCharacterServer = item.ServerName;
               return false;
            }
        });
        //
        getJsonElementById("characterSiblings").forEach((item) =>{

            if(item.ServerName == repCharacterServer){




                userProfiles(item.CharacterName);
            }
        });

    }
    userSiblings();

    // 해당 유저의 정보
    async function userProfiles(playerId) {
        const br = document.createElement("br");
        Image image = new Image();
        const res = await fetch(`/homework/userProfiles?playerId=${encodeURIComponent(playerId)}`);
        const data = await res.json();

        const div = document.createElement("div");
        div.append("@",data.ServerName , "  " ,
                            data.CharacterClassName , br.cloneNode(),
                            data.CharacterName , br.cloneNode() , data.ItemAvgLevel,
                            data.CharacterImage

        )

        characterInformation.appendChild(div);
    }




    // 쓸대없이 보여주는 html 태그들 삭제
    document.getElementById("repCharacter").remove();
    document.getElementById("characterSiblings").remove();

 });



 // 각 요소들 api Json 으로 정리 함수
 function getJsonElementById(getElementById){

     // 해당하는 api 요소들
     const userElementApi = document.getElementById(getElementById);
     const userElementTrim = userElementApi.textContent.trim(); // 띄워쓰기 삭제
     const userElementString = JSON.parse(userElementTrim); // string 가져오고

     // key 와 value 값을 가지기위해
     const userElementData = JSON.parse(userElementString); // 한번더 json 파싱

     return userElementData;
 }