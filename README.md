# 🐱 톰캣 구현하기 🐱

## 🔍 진행 방식

- 미션은 **기능 요구 사항, 미션 진행 요구 사항** 두 가지로 구성되어 있다.
- 두 개의 요구 사항을 만족하기 위해 노력한다. 특히 기능을 구현하기 전에 기능 목록을 만든다.
- 기능 요구 사항에 기재되지 않은 내용은 스스로 판단하여 구현한다.

## 🚀 `step1`: 미션 설명

간단한 HTTP 서버를 만들어보자.

저장소에서 소스코드를 받아와서 메인 클래스를 실행하면 HTTP 서버가 실행된다.

웹브라우저로 로컬 서버(http://localhost:8080)에 접속하면 Hello world!가 보인다.

정상 동작을 확인했으면 새로운 기능을 추가해보자.

## ⚙️ 기능 요구 사항

### 1. GET /index.html 응답하기

인덱스 페이지(http://localhost:8080/index.html)에 접근할 수 있도록 만들자.

Http11ProcessorTest 테스트 클래스의 모든 테스트를 통과하면 된다.

브라우저에서 요청한 HTTP Request Header는 다음과 같다.

```text
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```
- [x] process 의 socket 의 inputstream 의 buffer 를 읽어와 String 으로 변환한다.
- [x] request resource 경로를 파악하여 응답값을 생성한다.
- [x] 잘못된 경로 일시 404 를 반환한다.
- [x] 오류 메시지인 svg 이미지를 gzip 후 반환한다.

### 2. CSS 지원하기

인덱스 페이지에 접속하니까 화면이 이상하게 보인다.

개발자 도구를 열어서 에러 메시지를 체크해보니 브라우저가 CSS를 못 찾고 있다.

사용자가 페이지를 열었을 때 CSS 파일도 호출하도록 기능을 추가하자.


- 리다리렉션을 구현하지 않는다. :  많은 웹 페이지들이 최초요청 200 으로 응답받아 css, html 을 전부 처리한다.

- 파싱을 하면서 DOM Tree를 만들게 되는데, 파싱 도중에 link태그를 만나면 서버로 CSS파일 요청을 보낸다.
  다운받은 CSS를 파싱해서 CSSOM Tree를 만든다.

- [x] /css/styles.css 로 들어온 요청의 응답하는 css를 반환한다.

```text
GET /css/styles.css HTTP/1.1
Host: localhost:8080
Accept: text/css,*/*;q=0.1
Connection: keep-alive
```

### 3. Query String 파싱

http://localhost:8080/login?account=gugu&password=password으로 접속하면 로그인 페이지(login.html)를 보여주도록 만들자.

그리고 로그인 페이지에 접속했을 때 Query String을 파싱해서 아이디, 비밀번호가 일치하면 회원을 조회한 결과가 나오도록 만들자.

- [x] index.html 에서 요청하는 js,asset 를 반환환다.
- [x] login.html 의 최초 진입시 html 의 반환한다.
- [x] 회원 가입 페이지, 로그인 페이지로 이동한다.
- [] 쿼리스트링의 사용자 입력정보를 받아 DB 와 비교한다. 

## ✏️ 미션 진행 요구 사항

- 미션은 [jwp-dashboard-http-mission](https://github.com/speculatingwook/jwp-dashboard-http-mission) 저장소를 Fork & Clone해 시작한다.
- **기능을 구현하기 전 `README.md`에 구현할 기능 목록을 정리**해 추가한다.