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
   
