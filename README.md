# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

### 1단계 
- http://localhost:8080/index.html 로 접속했을 때 src/main/resources/static 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
- 서버로 들어오는 HTTP Request의 내용을 읽고 적절하게 파싱해서 로거(log.debug)를 이용해 출력한다.
- request line의 메소드가 GET이면 요청 url에 해당하는 파일을 리턴한다.
- ResourceLoader를 인터페이스로 선언하여 File방식과 ClassLoader방식을 사용할 수 있도록 한다.

### 2단계
- html, css,js, ico, png, jpg contents-type을 지원한다
- 헤더를 Map에 저장하여 원하는 값들을 쉽게 가져올 수 있도록 만든다
- Accept 헤더의 값과 파일의 확장자가 같다면 해당 값을 Content-Type으로 설정한다.
- 예를들어 Accept 헤더가 text/css이고 main.css를 요청하면 response의 Content-Type을 text/css로 바꾼다.
- ResponseHandler를 만들어 응답을 HttpResponse를 제작한다.

### 고민 사항
- index.html 파일을 읽어오는 방법으로 File과 ClassLoader 중 어떤 것을 사용할지 고민했습니다.
 File을 사용하면 파일 시스템의 실제 파일을 직접 읽기 때문에 파일 변경 사항에 즉시 반응할 수 있고, 디버깅이나 개발 과정에서 더 직관적이라는 장점이 있습니다. 
 하지만 애플리케이션을 빌드한 경우, src/main/resources 디렉토리에 있는 파일은 파일 시스템의 독립된 파일이 아니라 JAR 내부에 압축된 리소스가 되므로 File로는 접근할 수 없다는 문제가 있습니다.
 제가 내린 결론은 ResourcesLoader 인터페이스를 만들고 다형성을 이용하여 개발중에는 File, 배포시에는 ClassLoader 방식을 사용한다면 두 방식의 장점을 모두 살릴 수 있을 것 같습니다.