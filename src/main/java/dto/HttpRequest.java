package dto;

import java.util.Map;

// HttpRequest.java (DTO)
public record HttpRequest (String method,
         String path,
         String queryString,
         Map<String, String> headers,
         Map<String, String> cookies,
         String body){
}
