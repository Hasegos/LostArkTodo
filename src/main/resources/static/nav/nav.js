// 로그인했을때 로그아웃 클릭했을 때 나타나게끔
// DOMContentLoaded 를 사용해서 html 다 업로드된후 사용
document.addEventListener("DOMContentLoaded" , function(){
    const toggleButton = document.getElementById("toggle-logout");
    const logoutContainer = document.getElementById("logout-container");
    const toggleLogoutText = document.getElementById("toggle-logout_text");

    toggleButton.addEventListener("click", function() {
        if(logoutContainer.style.display === "none"){
            // 상단에 뜨는 넓이 크기는 로그인 이메일 넓이 만큼
            const textWidth = toggleLogoutText.offsetWidth;
            logoutContainer.style.width = textWidth + "px";
            // block 형태로 보여주기
            logoutContainer.style.display = "block";
            logoutContainer.style.margin = "5px 20px";
        }
        else{
            logoutContainer.style.display = "none";
        }
    });

     // 테마 색상을 변경
     // 페이지 로드 시 이전에 설정한 테마를 확인하고 적용하기
     /*
     document.addEventListener("DOMContentLoaded", function () {
         const theme = localStorage.getItem("theme") || "light";
         document.documentElement.setAttribute("data-theme", theme);
         updateButtonLabel(theme);
     });

         // 버튼 클릭 시 테마 전환하기
     document.getElementById("theme-toggle").addEventListener("click", function () {
         const currentTheme = document.documentElement.getAttribute("data-theme");
         const newTheme = currentTheme === "dark" ? "light" : "dark";

         // 새로운 테마 설정
     document.documentElement.setAttribute("data-theme", newTheme);
         localStorage.setItem("theme", newTheme); // 테마 설정을 localStorage에 저장
         updateButtonLabel(newTheme); // 버튼 라벨 업데이트
     });

         // 버튼 라벨을 테마에 따라 업데이트
     function updateButtonLabel(theme) {
         const button = document.getElementById("theme-toggle");
         if(theme === "dark"){
            button.innerHTML = '<i class="fa-solid fa-moon fa-2x"></i>';
            }
         else{
            button.innerHTML = '<i class="fa-regular fa-sun fa-2x"></i>';
         }
     }
     */
});