package utils;

import db.Database;
import httpconst.HttpConst;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    private static Map<String, String> paramMap = new HashMap<>();

    public static String[] readStatusLine(String request){
        String[] requestStatusLine = request.split(HttpConst.EMPTY_SPACE);
        String method = requestStatusLine[0];
        String url = requestStatusLine[1];
        String version = requestStatusLine[2];
        return requestStatusLine;
    }

    public static Map<String, String> parseRequestUrl(String url){
        String[] urlSplitList = url.split("\\?");
        String userInfo = urlSplitList[1];
        String[] userInfoList = userInfo.split(HttpConst.AMPERSAND);

        for (String s : userInfoList) {
            String[] keyValue = s.split(HttpConst.EQUALS);
            paramMap.put(keyValue[0], keyValue[1]);
        }

        return paramMap;
    }

    public static String extractExtension(String url){
        return url.split("\\?")[0].substring(url.lastIndexOf("."));
    }

    public static Map<String, String> parseBody(String bodyContents) {
        String[] bodySplitList = bodyContents.split(HttpConst.AMPERSAND);
        Map<String, String> bodyMap = new HashMap<>();
        for(String bodyInfo : bodySplitList){
            String[] bodyKeyValue= bodyInfo.split(HttpConst.EQUALS);
            bodyMap.put(bodyKeyValue[0], bodyKeyValue[1]);
        }
        return bodyMap;
    }

    // body 파싱

}
