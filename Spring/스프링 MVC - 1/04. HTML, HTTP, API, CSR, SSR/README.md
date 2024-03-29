# HTML, HTTP, API, CSR, SSR

### 정적 리소스
* 고정된 HTML 파일, CSS, JS, 이미지, 영상 등을 제공 

### HTML
* 동적으로 필요한 HTML 파일을 생성해서 전달 
* 웹 브라우저가 HTML을 해석

### HTTP API
* HTML이 아니라 데이터를 전달함
* 주로 JSON 형식 사용
* 다양한 시스템에서 호출 (앱, 다른 서버, JS 등)
* 데이터만 주고 받으며 이 데이터를 이용해 클라이언트에서 UI등을 처리

### SSR (서버 사이드 렌더링)
* 백엔드 
* HTML 최종 결과를 서버에서 만들어서 브라우저에 전달.
* 정적인 화면에 주로 사용
* 관련 기술 -> jsp, thymeleaf
* 화면이 정적이고 복잡하지 않을 때 사용
* 백엔드는 SSR 기술 필수.

### CSR (클라이언트 사이드 렌더링)
* 웹 프론트엔드
* HTML 결과를 자바스크립트를 사용해 브라우저에서 동적으로 생성
* 주로 동적인 화면에 사용
* 관련기술 -> React, Vue.js
* 복잡하고 동적인 UI
* 웹 프론트엔드 개발자 전문

### 백엔드는 어디까지 알아야 하나?
* 웹 프론트엔드 기술 학습은 옵션
* 위에보단 서버, DB, 인프라등의 수많은 기술 공부가 필요.
* 웹 프론트엔드도 깊이있게 하려면 오랜 시간 필요.