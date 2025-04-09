# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판





http://localhost:8080/index.html

Thread-0
New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 52458
request line : 
GET / HTTP/1.1  - 요청 내용
header line : 
Host: localhost:8080 - 요청이 향하는 도메인과 포트 번호
Connection: keep-alive - 연결을 끊지 않고 계속 유지할지 여부
sec-ch-ua: "Google Chrome";v="135", "Not-A.Brand";v="8", "Chromium";v="135" - User-Agent Client Hints 중 하나, 어떤 브라우저를 사용하는지 세밀하게 전달, 보안 최적화를 위한 브라우저 정보 전달
sec-ch-ua-mobile: ?0 - 모바일 브라우저인지(?0 는 모바일 아니고 데스크탑)
sec-ch-ua-platform: "macOS" - 운영체제 정보
Upgrade-Insecure-Requests: 1 - "HTTP -> HTTPS 로 업그레이드 원함", 보안 연결을 선호한다는 클라이언트의 요청
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 - 전통적인 브라우저 정보(브라우저 종류, OS 등), 서버가 브라우저별로 다르게 응답할 수 있게 한다
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7 - 클라이언트가 받아들일 수 있는 콘텐츠 종류, 서버는 여기 있는 타입 중 하나로 응답해야 한다, */*는 아무 형식이나 가능하다는 뜻
Sec-Fetch-Site: none - 요청의 출처(origin), none은 직접 입력했거나, 북마크 등에서 접근(외부 페이지 아님)
Sec-Fetch-Mode: navigate - 요청의 목적, navigate는 브라우저 주소창을 통해 페이지 이동하는 요청임을 의미
Sec-Fetch-User: ?1 - 유저의 명시적 동작(클릭 등)에 의해 발생한 요청인지 표시 (1? -> 유저의 명시적 동작 맞음, 뭔가 클릭하거나 입력)
Sec-Fetch-Dest: document - 이 요청의 결과물이 어디에 들어갈지 알려줌, document는 최상위 html 문서라는 뜻
Accept-Encoding: gzip, deflate, br, zstd - 클라이언트가 어떤 압축 방식의 응답을 받아들일 수 있는지 명시, br : Brotli, zstd : Zstandard
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7 - 클라이언트가 선호하는 언어 (한국어>영어(미국)>영어)
Cookie: Idea-fc96cb0e=075c23d7-a2d3-4d0f-9744-3392494e6dbb; JSESSIONID=CBA5FB79665C2D8B4718FCC0AAC3C8C7 - 클라이언트가 이전에 받은 쿠키를 서버에 다시 보냄, 여기서는 IDE 관련 쿠키와 세션 ID(JSESSIONID)
(empty line)


Thread-1
New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 52459
request line : 
GET /favicon.ico HTTP/1.1
header line : 
Host: localhost:8080
Connection: keep-alive
sec-ch-ua-platform: "macOS"
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36
sec-ch-ua: "Google Chrome";v="135", "Not-A.Brand";v="8", "Chromium";v="135"
sec-ch-ua-mobile: ?0
Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: no-cors
Sec-Fetch-Dest: image
Referer: http://localhost:8080/
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
Cookie: Idea-fc96cb0e=075c23d7-a2d3-4d0f-9744-3392494e6dbb; JSESSIONID=CBA5FB79665C2D8B4718FCC0AAC3C8C7
(empty line)


Thread-2
New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 52517
request line : 
GET /index.html HTTP/1.1
header line : 
Host: localhost:8080
Connection: keep-alive
sec-ch-ua: "Google Chrome";v="135", "Not-A.Brand";v="8", "Chromium";v="135"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "macOS"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36
text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
Cookie: Idea-fc96cb0e=075c23d7-a2d3-4d0f-9744-3392494e6dbb; JSESSIONID=CBA5FB79665C2D8B4718FCC0AAC3C8C7
(empty line)


Thread-3
New Client Connect! Connected IP : /0:0:0:0:0:0:0:1, Port : 52518
request line : 
GET /favicon.ico HTTP/1.1
header line : 
Host: localhost:8080
Connection: keep-alive
sec-ch-ua-platform: "macOS"
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36
sec-ch-ua: "Google Chrome";v="135", "Not-A.Brand";v="8", "Chromium";v="135"
sec-ch-ua-mobile: ?0
Accept: image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: no-cors
Sec-Fetch-Dest: image
Referer: http://localhost:8080/index.html
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
Cookie: Idea-fc96cb0e=075c23d7-a2d3-4d0f-9744-3392494e6dbb; JSESSIONID=CBA5FB79665C2D8B4718FCC0AAC3C8C7
(empty line)




## Concurrent 패키지

java.util.concurrent 패키지는 멀티스레드 환경에서 안정적이고 효율적으로 작업할 수 있게 해주는 고급 유틸리티 모음이다.

### Concurrent 패키지가 필요할 때

1. 동시에 여러 작업을 처리해야 할 때
- 웹 서버가 여러 클라이언트의 요청을 동시에 처리해야 할 때
  - ExecutorService, ThreadPoolExecutor

2. 작업을 병렬로 처리하되, 결과를 모아야 할 때
- 여러 파일을 동시에 읽고, 다 읽은 후에 하나로 합칠 때
  - Future, Callable, CompletionService

3. 여러 스레드가 공유 자원을 안전하게 사용할 때
- 큐에 동시에 데이터를 넣고 빼야 할 때
  - BlockingQueue, ConcurrentHashMap, ReentrantLock

### Concurrent 패키지를 사용하는 이유

1. 스레드 관리 자동화
- new Thread()로 매번 생성하고 start() 하는 방식보다,
- ExecutorService를 사용해서 스레드 풀을 만들어 두고, submit()으로 호출하는 방식이 더 관리하기 편하다.
- 스레드 풀을 사용하면 스레드를 재사용할 수 있고, shutdown()으로 정리할 수 있다.

2. 동기화
- 동기화란, 여러 스레드가 동시에 하나의 자원에 접근할 때, 충돌을 막는 것이다.
- 기존 방식인 synchronized 는 하나의 스레드만 접근 가능하게 lock 을 거는 방식이다. 
  - 많은 스레드가 대기하게 되었을 때 성능이 저하될 수 있다.
- concurrent 패키지의 동기화
  - ReentrantLock : synchronized 보다 더 세밀하게 제어가 가능하다.
  - BlockingQueue : 내부적으로 동기화 처리가 되어있다. 여러 스레드가 안전하게 데이터를 넣고 뺄 수 있다.
  - ConcurrentHashMap : 여러 스레드가 동시에 읽고 써도 안전하게 작동하도록 구현된 해시맵이다.

3. 성능 최적화
- 자바에서 자원이란, CPU, 메모리, 스레드, I/O 등인데, 이 중에서 "스레드 낭비"가 가장 흔하다.
- 요청마다 new Thread 로 새 스레드를 만들게 되면 CPU 의 context switching, 메모리 등의 스레드 생성 비용이 과도하게 든다.
- concurrent 패키지는 스레드 풀과 작업 큐를 사용해서 자원의 낭비를 줄인다.
  - 스레드 풀 : 스레드를 일정 갯수 만들어두고, 작업이 들어오면 만들어 둔 스레드를 사용한다.
  - 작업 큐 : 스레드가 바쁘면 작업 요청을 큐에 저장해두고, 처리할 수 있을 때 순차적으로 꺼내서 실행한다.
- 스레드를 필요한 만큼 만들어서 재사용하고, 작업 요청은 쌓아뒀다가 순서대로 처리하기 때문에 성능 최적화된 구조를 만들 수 있다.

4. 높은 유연성
- ExecutorService + Future 와 기타 도구들로 작업을 더 유연하게 제어할 수 있다.
- 작업 취소 : 제출한 작업을 도중에 중단할 수 있다.
  - future.cancel(true) 로 스레드에 interrupt() 신호를 보낼 수 있다.
  - 작업 내에서 Thread.interrupted()를 체크해줘야 작업을 취소할 수 있다.
- 타임아웃 : 결과를 기다리는 시간에 제한을 둘 수 있다. 너무 오래 걸리면 중간에 포기한다.
  - 예시) 2초만 기다린 후에 TimeoutException 을 발생시킨다.
```java
Future<String> future = executor.submit(() -> {
    Thread.sleep(5000);  // 5초 후 작업
    return "작업완료";
});

try {
    String result = future.get(2, TimeUnit.SECONDS); // 2초만 기다리기
} catch (TimeoutException e) {
    System.out.println("시간 초과");
}
```
- 병렬 작업 결과 수집 : 여러 작업을 동시에 수행하고 결과를 한꺼번에 수집할 수 있다. 
  - 

### Concurrent 패키지 대표 도구

- Executor, ExecutorService
- Executors
- Future
- Callable
- BlockingQueue
- ReentrantLock
- CountDownLatch, Semaphore, CyclicBarrier





