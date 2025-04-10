# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

공부 정리한 내용은 github Wiki에 작성
## 웹 서버 1단계 - index.html 응답
Thread 기반을 Concurrent 패키지를 사용하도록 변경.  
Request Line에서 path를 분리하여 정적 파일의 내용을 반환하도록 함.


## 웹 서버 2단계 - 다양한 컨텐츠 타입 지원
RequestHandler의 로직을 크게 입력, 출력으로 나누었다.  
입력을 읽어, RequestHeader로 변환해서 쉽게 원하는 header를 찾을 수 있는 로직을 추가했다.  
파일의 확장자에 따라 response의 Content-Type을 설정하도록 했다.  
예외가 발생하면 우선 RequestHandler로 던져 처리할 수 있도록 했다.  

