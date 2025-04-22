package webserver.request;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String version;
    private final Map<String, String> headers = new HashMap<>(); //hashmap -> map
    private final Map<String, String> queryParams = new HashMap<>();

    public HttpRequest(InputStream inputStream) throws IOException {
        loggerParse(inputStream);
    }

    private void loggerParse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line = br.readLine();
        if (line == null || line.isEmpty()) throw new IOException("Invalid HTTP Request: empty request line");

        String[] request_lines = line.split(" ");
        this.method = request_lines[0];
        String[] path_str = request_lines[1].split("\\?");
        this.path = path_str[0];

        if(path_str.length>1){
            String[] queryParameter = path_str[1].split("&");
            for(String param : queryParameter){
                String[] keyValue = param.split("=");
                queryParams.put(keyValue[0], URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
            }
        }

        this.version = request_lines[2];

        while(!(line = br.readLine()).isEmpty()){
            String[] header_strs = line.split(": ");
            headers.put(header_strs[0],header_strs[1]);
        }


        //Body 파싱
        if(method.equals("POST")){
            int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length","0"));
            char[] charBody = new char[contentLength];
            //요청 body를 최대 contentLength 만큼 읽어서 charBody 배열에 넣는다
            br.read(charBody,0, contentLength);
            String body = new String(charBody);

            System.out.println("======= REQUEST BODY =======");
            System.out.println(body);
            System.out.println("============================");

            String[] params = body.split("&");
            for(String param : params){
                String[] kv = param.split("=");
                queryParams.put(kv[0],URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }

            System.out.println(queryParams);
        }

    }

    public String getMethod(){
        return method;
    }

    public String getPath(){
        return path;
    }

    public String getVersion(){
        return version;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public Map<String, String> getQueryParams(){
        return queryParams;
    }
}
