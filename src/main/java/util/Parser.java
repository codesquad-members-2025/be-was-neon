package util;

import model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class Parser {

    public static void loggerParser(String line, HashMap<String, String> map, String type) {

        if(type.equals("requestLine")){
            String[] request_strs = line.split(" ");
            map.put("method",request_strs[0]);
            map.put("path",request_strs[1]);
            map.put("version", request_strs[2]);
        }
        else if(type.equals("header")){
            String[] header_strs = line.split(": ");
            StringBuilder value = new StringBuilder();
            for(int i = 1; i<header_strs.length; i++){
                value.append(header_strs[i]);
            }
            map.put(header_strs[0], value.toString());

        }
    }

    public static User requestPathParser(HashMap<String,String> requests) throws UnsupportedEncodingException {
        String[] parsedRequest = requests.get("path").split("\\?");
        String RequestPath = parsedRequest[0];
        String queryParams  = parsedRequest[1];
        String[] queryParameter = queryParams.split("&");
        HashMap<String, String> QueryParamMap = new HashMap<>();
        for(String q : queryParameter){
            String key = q.split("=")[0];
            String value = q.split("=")[1];
            value = URLDecoder.decode(value, StandardCharsets.UTF_8);
            QueryParamMap.put(key, value);
        }

        String userId = QueryParamMap.get("userId");
        String password = QueryParamMap.get("password");
        String name = QueryParamMap.get("name");
        String email = QueryParamMap.get("email");
        return new User(userId, password, name, email);
    }

}
