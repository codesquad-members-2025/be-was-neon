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
