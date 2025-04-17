package utils;

import db.Database;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    private static Map<String, String> paramMap = new HashMap<>();

    public static Map<String, String> parseUserInfo(String url){
        String[] urlSplitList = url.split("\\?");
        String userInfo = urlSplitList[1];
        String[] userInfoList = userInfo.split("&");

        for (String s : userInfoList) {
            String[] keyValue = s.split("=");
            paramMap.put(keyValue[0], keyValue[1]);
        }

        return paramMap;
    }

    public static String extractPath(String url){
        return url.split("\\?")[0];

    }

}
