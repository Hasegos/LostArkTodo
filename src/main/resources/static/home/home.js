const information = document.getElementById("information"); // 캐릭터 정보를 출력할 공간
const inputCharacter = document.getElementById("inputCharacter"); // 캐릭터 이름 입력

const characterInformation = document.getElementById("characterInformation");

// 캐릭터 정보
async function charactersInformation() {
    // 캐릭터 이름
    const repCharacter = inputCharacter.value;

    // 해당 안에 내용 초기화
    characterInformation.innerHTML = "";

    // 입력값이 없을 경우
    if(repCharacter == null || repCharacter == ""){
        alert("캐릭터 이름을 입력해주세요");
        return;
    }

    try{
        // 입력한 캐릭터 정보가져오기
        const userProfilesCall =  await fetch(`/home/userProfiles?playerId=${encodeURIComponent(repCharacter)}`);
        const userProfiles = await userProfilesCall.json();

        // 입력한 캐릭터의 장비 정보
        const userEquipmentCall = await fetch(`/home/userEquipment?playerId=${encodeURIComponent(repCharacter)}`);
        const userEquipment = await userEquipmentCall.json();

        console.log(userEquipment);

        const userEngravingsCall = await fetch(`/home/userEngravings?playerId=${encodeURIComponent(repCharacter)}`);
        const userEngravings = await userEngravingsCall.json();


        searchCharacter(userProfiles,userEquipment,userEngravings);


    }catch(e){
        console.error(e);
        alert("해당 캐릭터 정보가 없습니다.");
    }
}

// 캐릭터 찾기 함수
function searchCharacter(userProfiles,userEquipment,userEngravings){

    // 해당 정보 공간 만들기
    // 캐릭터 정보
    const divInformation = document.createElement("div");

    divInformation.style.backgroundColor = "#15181d";

    // 캐릭터 아이템 정보
    const divEquipment = document.createElement("div");

    // 캐릭터 각인 정보
    const divEngraving = document.createElement("div");

    // 각인 + 아이템 정보
    const informationPlusEquipment = document.createElement("div");

    informationPlusEquipment.style.marginBottom = "10px"
    informationPlusEquipment.style.marginLeft = "20px"

    // 캐릭터 아이템 정보 공간추가
    informationPlusEquipment.appendChild(divEquipment);

     // 캐릭터 각인 정보 공간 추가
    informationPlusEquipment.appendChild(divEngraving);

    // 캐릭터 정보 공간추가
    characterInformation.appendChild(divInformation);

    // 아이템 + 각인 추가된 div 를 공간추가
    characterInformation.appendChild(informationPlusEquipment);




    // 해당 정보 찾기
    // 캐릭터 정보
    searchCharacterInformation(userProfiles,divInformation);

    // 캐릭터 아이템 정보
    searchCharacterEquipment(userEquipment,divEquipment);

    // 캐릭터 각인 정보
    searchCharacterEngraving(userEngravings, divEngraving);
}

// 캐릭터 자체 정보
function searchCharacterInformation(userProfiles,divInformation){


    divInformation.style.display ="flex";
    divInformation.style.height = "300px";
    // text 묶음
    const divInformationText = document.createElement("div");
    // img 묶음
    const divInformationImg = document.createElement("div");
    const br = document.createElement("br");

    // 부모 밑 자식으로 요소 추가가능하게끔 (선택한 요소에 텍스트 삽입하도록)
    // 캐릭터 정보
    const CharacterClassName = document.createTextNode("직업 : "+ userProfiles.CharacterClassName);
    const CharacterName = document.createTextNode("캐릭터 이름 : " + userProfiles.CharacterName);
    const ItemAvgLevel = document.createTextNode("클래스 이름 : " + userProfiles.ItemAvgLevel);
    const ServerName = document.createTextNode("아이템 레벨 : " + userProfiles.ServerName);

    // 이미지 파일 생성
    const characterImg = new Image();
    characterImg.src = userProfiles.CharacterImage;
    characterImg.style.width = "200px";

    divInformationImg.append(characterImg);

    divInformationText.append(CharacterClassName,br.cloneNode(), CharacterName,br.cloneNode(),
            ItemAvgLevel,br.cloneNode(), ServerName,br.cloneNode(),
    );

    divInformation.append(divInformationText,divInformationImg);
}


// 캐릭터 장비 정보
function searchCharacterEquipment(userEquipment,divEquipment){

    // 아이템 테두리 css
    divEquipment.style.display = "flex";
    divEquipment.style.gap = "10px";
    divEquipment.style.marginBottom = "20px";

    let itemsPerPow = 6;
    let rowContainer = null;
    const br = document.createElement("br");

    userEquipment.forEach((item, index) => {

        // 해당 내용들은 안보이게끔
        if(item.Type === "나침반" || item.Type == "부적"){
            return true;
        }

        const span = document.createElement("span");
        span.style.display = "block"
        span.style.textAlign = "center";
        span.style.borderRadius = "5px";
        span.style.width = "40px";

        if(item.Type === "팔찌" || item.Type === "어빌리티 스톤"){

        }
        else {
            const tip = JSON.parse(item.Tooltip);
            console.log(tip);
            // 아이템 품질
            const quality = tip.Element_001.value.qualityValue;

            span.innerText = quality;
            console.log(quality);

            if(quality === 100){
                span.style.backgroundColor = "#ff5e00";
            }
            else if(90<= quality &&  quality < 100){
                span.style.backgroundColor = "#ff00dd";
            }
            else if(70<= quality &&  quality < 89){
                span.style.backgroundColor = "#0054ff";
            }
            else if(30<= quality &&  quality < 69){
                span.style.backgroundColor = "#00b700";
            }
            else if(10<= quality &&  quality < 29){
                span.style.backgroundColor = "#ffc000";
            }
            else {
                span.style.backgroundColor = "#c00000";
            }
        }

        // 첫번째 6개 이후 7개 출력
        if(index === 0  || index === 6){

            rowContainer = document.createElement("div");
            rowContainer.style.display ="flex";
            rowContainer.style.flexDirection = "column";
            rowContainer.style.gap ="5px";

            divEquipment.appendChild(rowContainer);
        }
        // 품질과 아이템 이미지 합칠 공간
        const divQuality = document.createElement("div");
        // 아이템 이미지
        const userEquipmentImage = new Image();
        userEquipmentImage.src = item.Icon;
        userEquipmentImage.style.width = "40px";
        userEquipmentImage.style.height = "45px";
        userEquipmentImage.style.borderRadius = "7px";
        userEquipmentImage.style.marginRight = "10px";

        // 팔찌 , 어빌리티 스톤은 품질 출력 x
        if(item.Type === "팔찌" || item.Type === "어빌리티 스톤"){
            divQuality.append(userEquipmentImage);
        }
        else {
            divQuality.append(userEquipmentImage ,br.cloneNode() ,span );
        }

        // 유물 일경우
        if(item.Grade == "유물"){
            userEquipmentImage.style.backgroundImage = "linear-gradient(135deg , rgb(72,34,11), rgb(162,64,6))";
        }
        // 고대일 경우
        else if(item.Grade == "고대"){
            userEquipmentImage.style.backgroundImage = "linear-gradient(135deg , rgb(61,35,37), rgb(220,201,153))";
        }
        // 각 이미지 + 내용 합친 공간
        const itemContainer = document.createElement("div");
        itemContainer.style.display = "flex";
        itemContainer.style.fontSize = "15px";


        // 귀걸이,  목걸이, 반지 일떄 연마효과 추가
        if(item.Type == "귀걸이" || item.Type == "목걸이" || item.Type =="반지"){
            const div = document.createElement("div");
            // JSON 문자열 형태로 다시만들기
            const tip = JSON.parse(item.Tooltip);

            // 연마효과 가져오기
            const polishingEffectInformation = tip.Element_005.value.Element_001;
            // BR태그 기준으로 짜르기
            const polishingEffectSplit = polishingEffectInformation.split("<BR>");


            // 짜른걸 새로운 배열로 가져오기
            const  polishingEffect = polishingEffectSplit.map(line => {
                return line.replace(/<\/img>/, "").trim(); // </img> 태그 제거
                })
                .map(line => {
                return line.replace(/<img[^>]*>/, "").trim(); // <img> 태그 제거 후 순수 텍스트만 반환
            });

            for(let effect of polishingEffect){
                div.innerHTML += effect + "<br>";
            }
            itemContainer.append(divQuality,div);
        }
        else if(item.Type == "팔찌"){
            const div = document.createElement("div");
            const tip = JSON.parse(item.Tooltip);

            const braceletEffectInformation = tip.Element_004.value.Element_001
            const braceletEffectSplit = braceletEffectInformation.split("<BR>");

            const braceletEffect = braceletEffectSplit.map(line => {
                return line.replace(/<\/img>/, "").trim();
            }).map(line => {
                return line.replace(/<img[^>]*>/, "").trim();
            })


            for(let effect of braceletEffect){
                div.innerHTML += effect + "<br>";
            }
            div.style.width = "390px";
            itemContainer.append(divQuality, div);

        }else if(item.Type == "어빌리티 스톤"){
            const div = document.createElement("div");
            const tip = JSON.parse(item.Tooltip);
            const br =document.createElement("br");
            let i = 0;
            const braceletEffectInformation = tip.Element_006.value.Element_000.contentStr;
            for(const item in braceletEffectInformation){

               // 각 이름, 아이콘, 레벨 포함 div 태그
               const divText = document.createElement("div");

               const htmlStr = braceletEffectInformation[item].contentStr;
                // 모든 html 태그 삭제하기
               const textOnly = htmlStr.replace(/<[^>]*>/g, "");
               // 돌격대장 Lv.2 이렇게 다뽑힘
               const nameMatch = textOnly.match(/\[([^\]]+)\]/);

               // 이때 돌격대장만 뽑기
               const name = nameMatch ? nameMatch[1] : "알 수 없음";

               const nameSpan = document.createElement("span");
               nameSpan.textContent = name;

               // 레벨만 뽑기
               const levelMatch = textOnly.match(/Lv\.(\d+)/);
               const level = levelMatch ? `Lv.${levelMatch[1]}`: "";

               // 각인 이미지
               const icon = new Image();

               icon.src = "https://cdn-lostark.game.onstove.com/2018/obt/assets/images/pc/profile/img_engrave_icon.png";
               icon.style.width = "26px";
               icon.style.height = "26px";
               icon.style.objectFit = "none";
               icon.style.objectPosition = "0px 0px" ;
               icon.style.verticalAlign = "middle";

               if(i == 2){
                    nameSpan.style.color = "#cf2429";
               }
               else{
                    nameSpan.style.color = "#ff6000";
               }

               divText.append(nameSpan,  "  ");
               divText.appendChild(icon);
               divText.append("  " + level , br.cloneNode());

               div.style.lineHeight = "1";
               div.appendChild(divText);
               i++;

            }
            itemContainer.append(divQuality, div);
        }
        else{
            itemContainer.append(divQuality, item.Name);
        }

        rowContainer.appendChild(itemContainer);
    });
}

// 캐릭터 각인 정보
function searchCharacterEngraving(userEngravings, divEngraving){

    // 각인 테두리 css
    divEngraving.style.display = "flex"
    divEngraving.style.flexDirection = "column";
    divEngraving.style.width = "200px";

    // 클랙스 : 각인
    const engraving = document.createElement("div");
    engraving.className += "engraving";
    engraving.innerText = "각인";
    engraving.style.textAlign = "center";
    engraving.style.fontSize = "19px";
    engraving.style.marginBottom = "15px";

    divEngraving.append(engraving);

    // 각인 이름 + 유물각인 단계
    for(item of userEngravings.ArkPassiveEffects){
        const divEngravingItem = document.createElement("div");
        divEngravingItem.style.marginBottom = "10px";
        divEngravingItem.style.color = "#FF6000";

        // 돌 각인 정보
        const EngravingItemStone = new Image();

        EngravingItemStone.src = "https://cdn-lostark.game.onstove.com/2018/obt/assets/images/pc/profile/img_engrave_icon.png";
        EngravingItemStone.style.width = "26px";
        EngravingItemStone.style.height = "26px";
        EngravingItemStone.style.objectFit = "none";
        EngravingItemStone.style.objectPosition = "0px 0px" ;

        // 유각 정보
        const Engraving = new Image();

        Engraving.src = "https://cdn-lostark.game.onstove.com/2018/obt/assets/images/pc/profile/img_engrave_icon.png";
        Engraving.style.width = "26px";
        Engraving.style.height = "26px";
        Engraving.style.objectFit = "none";
        Engraving.style.objectPosition = "-110px 0px" ;

        // 어빌리티 스톤 레벨
        const AbilityStoneLevelspan = document.createElement("span");
        AbilityStoneLevelspan.textContent = "   Lv. " +item.AbilityStoneLevel;
        AbilityStoneLevelspan.style.color = "#00b5ff";

        // X 색상 추가
        const X = document.createElement("span");
        X.textContent = " x ";
        X.style.color = "#e4ba27";

        if(item.AbilityStoneLevel == null){
            divEngravingItem.append(item.Name , Engraving, X , item.Level);
        }else{
            divEngravingItem.append(item.Name , Engraving, X , item.Level , EngravingItemStone, AbilityStoneLevelspan);
        }

        divEngraving.append(divEngravingItem);
    }

}
// 중복클릭 방지
let isProcessing = false;
information.addEventListener("click",function (){

  if(isProcessing) return;
    isProcessing = true;

    charactersInformation();
    setTimeout(() => {
            isProcessing = false;
        },1500);
});
