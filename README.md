# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

### 1단계 
- http://localhost:8080/index.html 로 접속했을 때 src/main/resources/static 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
- 서버로 들어오는 HTTP Request의 내용을 읽고 적절하게 파싱해서 로거(log.debug)를 이용해 출력한다.
- request line의 메소드가 GET이고 /index.html을 요청한다면 index.html파일을 리턴한다.

