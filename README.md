# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

## 1. index.html 응답

### static html file 응답

- http://localhost:8080/index.html 접속시 src/main/resources/static/index.html 파일을 응답

### HTTP Request 출력

- HTTP Request를 콘솔에 출력
- 적절하게 파싱 후 `log.debug()`로 출력

### 구조 변경

- `Thread` 기반 구조에서 `Concurrent` 기반 구조로 변경

## 2. 다양한 컴텐츠 타입 지원

### MIME Type 지원

- `html`, `css`, `js`, `ico`, `png`, `jpg` 에 대한 MIME Type 지원

## 3. GET 회원가입

### GET 으로 회원가입 기능

- `http://localhost:8080/register.html` 접속시 회원가입 폼을 응답
- Request parameter로 `userId`, `password`, `name`, `email`을 받음
- 파싱 후 `model.User`클래스에 저장

### 품질 개선

- 유지보수 하기 용이하도록 코드 리팩토링
