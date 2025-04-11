# be-was-2024
## 웹 서버의 동작 방식

클라이언트(웹 브라우저, 모바일 앱 등)가 보낸 HTTP 요청을 받고, 이에 대한 **HTTP 응답**을 반환하는 역할을 한다

💡 **웹 서버의 기본 동작 과정**

### **1. 클라이언트가 HTTP 요청을 보냄**
[HTTP](https://www.notion.so/HTTP-1ce7afc044e98089974edb729ac0fc3e?pvs=4)

- 브라우저에서 [http://example.com](http://naver.com) 요청을 보냄
- 요청은 TCP 연결을 통해 웹 서버의 특정 **포트(예: 80, 443, 8080 등)** 로 전달됨

### **2. 웹 서버가 요청을 처리할 프로세스 또는 스레드 할당**

[스레드와 프로세스](https://www.notion.so/1ce7afc044e9807b918ed91f4c4f49c0?pvs=21)

- 웹 서버는 요청을 받으면 새로운 프로세스를 생성하거나 스레드를 생성하여 요청을 처리함.
- 사용되는 방식에 따라 다르게 동작함 (멀티 프로세스 모델 vs 멀티 스레드 모델)

  [웹 서버의 실행 방식 (프로세스 vs 스레드)](https://www.notion.so/vs-1d07afc044e980b3a001e2426f51b863?pvs=21)

  **→** Java 기반 웹 서버는 하나의 프로세스가 여러 개의 스레드를 생성하여 요청을 처리하는 방식인 **멀티 스레드(Multi-Thread) 모델**

  **→** `Thread` 또는 `ExecutorService`를 사용하여 **멀티 스레드 웹 서버**를 구축할 수 있음 (비동기 처리가 필요하다면 `CompletableFuture` 또는 `Reactor`(Spring WebFlux) 등을 활용할 수 있음)

  → Java에서 멀티스레드 프로그래밍을 할 때 Thread 클래스를 사용하여 직접 스레드를 만들 수 있지만, Java의 기본 스레드 모델은 관리가 어렵고 효율적이지 않기 때문에 (동시성 문제를 해결하기 어려움)**`java.util.concurrent` 패키지**를 이용하여 더 성능 좋고 안정적인 멀티스레드 프로그램을 만든다! → `ExecutorService` (스레드 풀), `Future`와 `Callable`, ReentrantLock 등을 사용

  [자바 Concurrent 패키지](https://www.notion.so/Concurrent-1d07afc044e9806a8f12cd319f372b72?pvs=21)


### **3.  요청을 분석하고 필요한 데이터 처리**

- 정적 리소스(HTML, CSS, 이미지 등)를 제공하거나,
- 동적 리소스(데이터베이스 조회, API 호출 등)를 처리함.

### **4. 클라이언트에게 HTTP 응답 전송**

- 요청 처리 결과를 HTTP 응답(Response) 형태로 클라이언트에게 반환함.

참조 :

[https://www.youtube.com/watch?v=mcnJcjbfjrs](https://www.youtube.com/watch?v=mcnJcjbfjrs)