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
- ResponseHandler를 만들어 HttpResponse를 제작한다.

### 3단계
- 회원가입 벼튼을 누르면 /registration/index.html을 응답한다.
- 경로가 디렉토리명인 경우 디폴트로 index.html을 리턴한다.
- request url의 파라미터를 파싱하여 model.User에 저장한다.

### 4단계
- 회원가입을 Get에서 post로 바꾼다.
- Get으로 회원가입을 시도할 경우 실패해야한다.
- Request객체를 만들어 request line, header, body를 관리한다.
- 데이터를 읽어 올때 readLine()이 아닌 Content-Length의 바이트 수만큼만 읽어온다
- Dispatcher에서 다른 메소드들을 사용할 수 있도록 한다.
- Dispathcer의 요청을 Handler에서 적절히 처리할 수 있는 구조를 만든다
- 잘못된 요청을 보낸 경우 에러 페이지를 리턴한다.

### 5단계
- 쿠키와 세션을 이용하여 로그인을 구현한다.
- 로그인이 성공한 경우 index.html로 이동한다.
- 로그인이 실패한 경우 /login/failed.html로 이동한다.
- 세션 객체에서 세션아이디를 생성하고 User 정보를 저장한다.
- 로그인이 성공하면 Response Header에 세션 아이디를 쿠키에 담아 보낸다.
- 쿠키 Path값은 "/"로 설정한다.
- 로그아웃하는 경우 세션을 무효화시킨다.

### 고민 사항
- index.html 파일을 읽어오는 방법으로 File과 ClassLoader 중 어떤 것을 사용할지 고민했습니다.
 File을 사용하면 파일 시스템의 실제 파일을 직접 읽기 때문에 파일 변경 사항에 즉시 반응할 수 있고, 디버깅이나 개발 과정에서 더 직관적이라는 장점이 있습니다. 
 하지만 애플리케이션을 빌드한 경우, src/main/resources 디렉토리에 있는 파일은 파일 시스템의 독립된 파일이 아니라 JAR 내부에 압축된 리소스가 되므로 File로는 접근할 수 없다는 문제가 있습니다.
 제가 내린 결론은 ResourcesLoader 인터페이스를 만들고 다형성을 이용하여 개발중에는 File, 배포시에는 ClassLoader 방식을 사용한다면 두 방식의 장점을 모두 살릴 수 있을 것 같습니다.

- 헤더들을 Map에 저장하는 작업을 수행하던 중 ,를 이용헤서 각 헤더의 값들을 분류하였습니다. 하지만 ,로 값들을 구분하는게 아닌 ;으로 구분하거나 헤더의 값 자체에 ,를 사용하는 경우가 있었습니다.
  HTTP 명세상 “comma-separated list”로 정의된 헤더들을 따로 정의하여 해당 헤더들만 ,로 구분하도록 했습니다.

- request url이 디렉토리인 경우 디폴트로 index.html을 리턴하기 위한 방법을 고민했습니다. 처음에는 "."이 없는 경로애 대해서 디렉토리라고 판단했지만 안전하지 않은 방법이었습니다.
  FileResourceLoader에선 File::isDirectory를 이용하는 방식을 사용했지만 ClasspathResourceLoader에선 사용할 수 없었습니다.
  ClasspathResourceLoader에선 기본적으로 디렉토리에 대한 요청이라 생각하고 url에 index.html을 붙여 InputStream을 찾았습니다. 해당 스트림이 null 이면 원래 경로로 찾고
 그래도 null 이면 exception을 발생시키는 방법으로 처리했습니다.

- ResourceLoader 리팩토링에 많은 고민을 했습ㄴ다. 기존에는 fileToBytes()를 오버라이딩 하여 각 클래스에서 사용하였지만 디폴트 페이지 설정 기능을 구현하면서
 단순히 파일을 변환하는 과정은 공통적인 코드를 사용하게 되었습니다. 이에 각 클래스에서는 url의 파일을 읽기 위한 InputStream을 구하는 메소드를 구현하고 
 해당 스트림을 이용하여 바이트 배열을 리턴하는 메소드를 디폴트 메소드로 만들어 처리했습니다.

- 회원가입 버튼을 누른 후 홈 화면으로 리다이렉트 시키기 위한 방법을 고민햇습니다. 단순히 302헤더를 메소드로 만들 수 도 있었지만 앞으로 발생할 요청에 따라 헤더를 동적으로 만들면 
 좋을것 같아 빌더 패턴을 적용하였습니다. 각 요청에 대한 결과를 Response객체에 담아 ResponseHeaderFactory에서 사용할 수 있도록 구조를 만들었습니다.

- request body를 읽기 위해 InputStream을 사용하여 Content-Length만큼 읽어오던 중 문제가 발생했습니다. read()를 한 경우 스레드가 sleep해서 새 요청을 기다리기만 하는 것이 문제였습니다.
 원인은 BufferedReader와 InputStream을 함께 사용하다 보니 BufferedReader가 InputStream을 먼저 읽어버려서, InputStream의 위치가 바디가 아니라 그 뒤에 가 있기 때문이었습니다.
 BufferedReader를 제거하고 전부 InputStream을 사용하여 순차적으로 처리 할 수 있도록 하였습니다.

- Dispatcher와 핸들러 구조를 어떻게 짜는것이 객체지향적일지 고민이 되었습니다. 고려해본 방식은 스프링과 같은 방식은 하나의 클래스 안에 요청 결로별 메소드를 만들어서 도메인별로 통일된 클래스를 가질 수 있지만
 어노테이션이 없다보니 if-else가 많이 생길것 같아 제외했습니다. 전략패턴을 이용하여 Http 메소드 별로 다른 핸들러를 구현하는 방식도 고민했지만 하나의 메소드에서도 실행할 기능이 다르기 때문에 
 메소드 별로 나누는 것은 크게 의미가 없다고 생각되었습니다. 결과적으로는 Handler 인터페이스를 구현하는 핸들러 구현체들을 만들고 이를 MethodResolver에서 생성해 사용하는 방식을 사용하였습니다.

- MethodResolver에서 Enum을 사용할지 Map을 사용할지 고민이 되었습니다. enum으로 관리하는 것이 관련 메소드나 타입으로서의 의미가 있어 좋을것 같았지만 핸들러를 구하기 위해 전체 enum을
 순회해야 한다는 점이 조금 맘에 들지 않았습니다. 이에 대한 해결책으로 MethodResolver내에 내부 enum을 만들고 해당 enum을 사용하는 enumMap을 이용해 조회를 하도록 수정했습니다.
 이방식으로 조회 속도와 enum의 장점을 모두 잡았다고 생각합니다.