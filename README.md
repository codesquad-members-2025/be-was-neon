# be-was-2024
코드스쿼드 백엔드 교육용 WAS 2024 개정판

## HTTP Response

### HTTP Response 란?

클라이언트(브라우저)의 요청(Request)에 의해 서버가 응답하는 메시지이다. 웹에서 HTML, 이미지, CSS 등을 줄 때 이 형식으로 응답한다.

### HTTP Response 의 구성

Status Line         : 상태 줄
Response Headers    : 응답 헤더
Blank Line          : 빈 줄 (헤더와 바디 구분)
Response Body       : 실제 데이터

```jsx
HTTP/1.1 200 OK
Content-Type: text/html; charset=utf-8
Content-Length: 27

<h1>Hello, Web!</h1>
```

### Content-Type 이 중요한 이유

브라우저는 Content-Type 을 보고 이 파일이 HTML인지, CSS인지, 이미지인지 판단해서 올바르게 렌더링한다.

<br>

## MIME Type (Multipurpose Internet Mail Extensions Type)

MIME Type 은 서버가 클라이언트에게 전송하는 **파일의 형식**을 알려주는 문자열이다. 브라우저는 이 값을 읽고 데이터를 어떤 형식으로 해석할지 결졍한다.

### MIME Type 구조

기본 구조는 <타입>/<서브타입> 이다.
- 타입은 파일의 종류, 서브타입은 파일의 포맷이다.

파일 확장자        MIME Type
.html           text/html
.css            text/css
.js             application/javascript
.png            image/png
.jpg            image/jpeg
.ico            image/x-icon

### MIME Type 이 중요한 이유

정확한 MIME Type 을 알지 못하면 브라우저는 파일을 제대로 표시할 수 없다.
서버가 HTML 파일을 전송하면서 Content-Type: text/html 헤더를 보내지 않으면 브라우저는 그 내용을 일반 텍스트로 오해할 수 있다.


### Content-Type 설정

```java
private String getContentType(String path) {
    if (path.endsWith(".html")) return "text/html; charset=utf-8";
    if (path.endsWith(".css")) return "text/css; charset=utf-8";
    if (path.endsWith(".js")) return "application/javascript";
    if (path.endsWith(".png")) return "image/png";
    if (path.endsWith(".jpg")) return "image/jpeg";
    if (path.endsWith(".svg")) return "image/svg+xml";
    if (path.endsWith(".ico")) return "image/x-icon";
    return "application/octet-stream";  // 기본값(바이너리 데이터)
}
```

- charset : character set 의 줄임말이다.
- utf-8 : 문자를 utf-8 방식으로 인코딩 했다는 뜻이다.
- 즉, "text/html; charset=utf-8" 헤더의 의미는 이 응답의 본문은 HTML 이고, 본문에 있는 문자는 UTF-8 방식으로 인코딩되어 있다는 뜻이다.




