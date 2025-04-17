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
        String path = requests.get("path"); // "/create?userId=abc&..."
        String[] parsed = path.split("&");

        String userId = "";
        String uri_path = "";
        String password="";
        String name = "";
        String email = "";
        int parseIdx = 0;
        for(int i = 0; i<parsed.length; i++){
            if(i == 0){ // "/create?userId=abc
                String[] path_str = parsed[0].split("\\?");
                uri_path = path_str[0]; // "/create
                System.out.println(Arrays.toString(path_str));
                parseIdx = path_str[1].indexOf('='); // "userId=abc
                userId = path_str[1].substring(parseIdx+1, path_str[1].length());
            }
            else if(i == 1){
                parseIdx = parsed[1].indexOf("=");
                String encoded = parsed[1].substring(parseIdx+1, parsed[1].length());
                name = URLDecoder.decode(encoded, StandardCharsets.UTF_8);
            }
            else if(i == 2){
                parseIdx = parsed[2].indexOf("=");
                email = parsed[2].substring(parseIdx+1, parsed[2].length());
                if(email.contains("%40")) {
                    email= email.replace("%40","@");
                }
            }
            else if(i == 3){
                parseIdx = parsed[3].indexOf("=");
                password = parsed[3].substring(parseIdx+1, parsed[3].length());
            }
        }


        return new User(userId, password, name, email);
    }
}
