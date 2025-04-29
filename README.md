# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

## Step 1,2
> * RequestHandler를 객체 지향적으로 분리하려 함 -> 요청과 응답으로 나누어서 분리
> * ContentType은 파일 확장자를 enum에 정의된 상수들로 매핑하여 MIME 타입 반환하도록 구현 -> 미리 정의되지 않은 확장자의 경우 기본값 반환


1. **요청 처리 (RequestHandler.java)**
   - 클라이언트 소켓 연결을 수락하고, BufferedReader를 이용하여 요청을 파싱함.
   - 기본 경로 처리를 수행 (`/` 요청은 `/index.html`로 매핑).
   - **정적 리소스 로딩**: `ClassLoader.getResourceAsStream("static" + path)`를 사용하여 클래스패스 내의 static 폴더에서 리소스를 로드.
   - 리소스가 존재하지 않으면 404 응답을, 존재하면 파일 내용을 `readAllBytes()`로 읽어 200 응답으로 전송함.
   - **자원 관리**: try-with-resources 구문을 활용하여 InputStream이 블록 종료 시 자동으로 close되도록 보장


2. **Content-Type 매핑 (ContentTypeMapper.java)**
   - 파일 확장자를 enum에 정의된 상수들로 매핑, MIME 타입을 반환하도록 구현.
   - enum의 정적 메서드 `getContentType(String path)`를 통해, 파일 경로에서 확장자를 추출한 후 적절한 MIME 타입을 반환함.
   - 정해진 개수, 표현방식 다양 -> enum 이 적절


3. **HttpRequest**
   - HTTP 요청의 첫 번째 줄(요청라인)을 읽어 메서드, 경로, 버전, 헤더 등을 파싱하여 저장.
   - 요청 헤더를 `Map<String, String>` 형태로 저장
   - 중요한 헤더만 필터링 메서드 -> 로그 위해


4. **HttpResponse**
   - HTTP 응답 헤더를 생성하고 응답 본문을 전송하는 역할을 담당.
   - 200 OK와 404 Not Found 응답 메서드를 제공

Step 7-3
> 내가 원하는 동작 흐름
>   1.	사용자가 index.html → “회원가입” 버튼 클릭
>   2.	→ registration/index.html로 이동 (회원가입 폼)
>   3.	→ GET /register?userId=... 요청 전송
>   4.	서버가 처리 후 → index.html로 리다이렉트
>   5.	index.html이 열리면서 “회원가입 성공!” 메시지를 팝업으로 띄움

1. GET 쿼리 파라미터 파싱 로직 구현 및 RequestParser 리팩토링
   •	HttpRequest에 parameters 필드 추가 (쿼리 파라미터 저장)
   •	RequestParser에서 ? 포함된 URL을 파싱해 key-value 파라미터 추출
   •	split() 대신 indexOf() + substring() 방식으로 쿼리 파라미터 파싱
   •	빈 파라미터는 무시 (key만 있는 경우 무시)
   •	전체 요청 파싱 구조 정리 (parseHeader, parseQuery 메서드로 분리)
   •     UTF-8으로 디코딩

2. Handler 인터페이스 구현
    * 모든 핸들러는 공통 인터페이스 handler를 구현

3. Dispatcher 구현
   * 	StaticFileHandler: 정적 리소스 처리 
   * 	경로가 /일 경우 /index.html로 리다이렉트
   * 	UserCreateHandler: 회원가입 처리 (/user/create)
   * 	중복 ID가 있을 경우 alert 팝업 후 /registration/index.html로 리다이렉트
   * 	회원가입 성공 시 /index.html로 리다이렉트
   * 	NotFoundHandler: 등록되지 않은 URI에 대해 404 응답 반환

4. 정적요청 핸들러, 회원가입핸들러, notfound핸들러 구현
   * 정정요청 -> "/"면 index.html로 리다이랙츠
   * 회원가입 -> 중복id면 회원가입 실패, 회원기입완료 후 index.html로 리다이랙트

5. httpresponse에 rediect 메서드 추가

6. RequestHandler는 디스패처로 핸들러 실행

## Step 8-1
* body의 구조가 form(form-urlencoded)인지 판단해서 바로 parameters에 map으로 저장할까 했지만, 
* 생성자 구조 통일 위해서 body를 빈 문자열에 저장하고
* createuserhandler를 실행할때 map으로 다시 파싱하는 구조로 구현
  * 파싱 유틸은 FormBodyParser 클래스 구현

* body를 bufferesreader로 읽고 있어서 char로 읽었는데, 
* contetnt-length는 byte 단위로 보내니까, char[]로 읽으면 문자 인코딩 떄문에 길이 가 안 맞을 수 있음
* 따라서 inputstream으로 byte[] 읽는게 더 정확한 방식
  * parseRequest(BufferedReader br, InputStream in)
      * 헤더는 br로, 바디는 in으로 분리해서 읽으려 했으나 -> BufferedReader.readLine()으로 헤더를 읽을 때 내부 버퍼를 한꺼번에 읽기 때문에 in.read()가 block 상태에 빠짐
  * 따라서 전체 요청을 InputStream 하나로 파싱
    * readLine(InputStream)으로 request-line, headers 직접 읽음 
    * Content-Length 만큼 byte[]로 정확히 읽어서 body 문자열 생성

## Step 8-2
**에러 처리**
* 404도 에러 클래스로 만들어야 하나 고민했지만,
* 에러 클래스가 필요한 504나 500 같은 예외는 "이 요청은 맞는데, 처리 도중 문제가 생긴 경우"에 던지는 비즈니스,서버 오류 예외인 반면
* 405(not found)는 "핸들러 조차 없다"는 상황이기 때문에 어떤 핸들러도 선택되지 않음을 표현하기 위해 notfoundhandler를 그대로 유지
   * RequestHandler → Dispatcher.getHandler() → ExceptionHandler → core.handle() 흐름에서 
   * 핸들러가 무조건 있어야 파이프라인이 끊기지 않고, 예외 처리 데코레이터(ExceptionHandler)도 정상적으로 감쌀 수 있다.
   * Dispatcher가 “핸들러가 없으니 바로 404를 던진다”면, 핸들러 인터페이스(Handler) 호출 지점이 사라져 버려서 파이프라인이 어색해짐.

**데코레이션 패턴**
* ExcpetionHandler 데코레이터
  * 핸들러를 감싸서, HttpException은 상태별로 처리하고, 그 외는 500으로 변환
  * 데코레이터 패턴(방식)을 구현하는 방법이 delegated 패턴

  
**설계**
1. Session 클래스 구현 (사용자 1명의 로그인 상태 나타냄)
    * 개별 세션 객체, 유효 시간/만료 판단 기능 보유
2. SessionManager  (서버 전체 세션 관리)
    * 전체 세션 저장소 (Map(동시성 문제->ConcurrentHashMap<> 사용)), 세션 생성/조회/삭제 기능
3. SessionCleaner (백그라운드에서 만료 세선 정리)
    * 5분마다 백그라운드로 만료 세션을 자동 삭제
    *  디스패처에 isexpired()메서드를 놓고 "요청 시 검사"를 하려 했으나
    * was 프로젝트를 하는 거니까 백그라운드에서 검사해주는게 맞다고 생각해 scheduledexcutor로 5분마다 검사
    * 실제로는 요청 시 검사(스프링부트) + 백그라운드 정리(tomcat) 를 함께 사용 ->  ➜ 즉시 반응성과 메모리 관리를 동시에 만족
4. CookiePaser (요청 헤더에서 SID 추출)
5. UserLoginHandler (로그인 처리: 세션 생성 + Set-Cookie 응답)
6. UserLogoutHandler (로그아웃 처리: 세션 제거 + 쿠키 만료)
7. AuthHandler (로그인 확인 데코레이터)

**리팩토링**
* HTTP 요청을 식별하기 위해 메서드랑 경로를 묶은 RouteKey라는 값 객체를 만듬 
    * RouteKey는 Map의 key로 사용되기 때문에, equals()와 hashCode()를 오버라이딩해서
      'method'와 'path' 값이 같으면 같은 요청으로 간주하도록 처리.  -> "method와 path 값이 같으면, 메모리 주소가 달라도 같은 key로 간주되게 하기 위해.
      * Java의 HashMap은 key를 찾을 때 **hashCode()**로 먼저 대충 비슷한 후보를 찾고(성능 최적화), **equals()**로 최종적으로 "진짜 같은지" 비교.
      * Java는 기본적으로 객체의 메모리 주소로 hashCode를 계산하고,equals()도 메모리 주소 비교
      * 만약 오버라이딩 해주지 않으면 무리 method, path 값이 같아도 다른 객체면 다르게 본다.
    * Dispatcher에서는 이 RouteKey를 key로 사용하는 Map을 만들어서,
    요청이 들어올 때마다 핸들러를 찾아 줌.

* Static Inner Class (Holder 패턴) 방식
  * 기본 SessionManager는 클래스가 처음 로딩될 때 instance를 바로 만듬.
  * 멀티 스레드 동시성은 안전하지만, 지연초기화(lazy loading)가 아님.
  * 그냥 Eager Initalization(미리 만들어놓는) 방식
  * Holder 패턴 방식
    * 완벽한 Thread-Safe : JVM이 클래스 로딩을 보장하기 때문에, synchronized 없이도 안전함
    * Lazy Loading : getInstance()가 처음 호출될 때 내부 Holder 클래스가 로딩됨
    * 퍼포먼스 최적화 : synchronized 락 없이도 빠르고 안전하게 동작함
