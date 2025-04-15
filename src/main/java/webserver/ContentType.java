package webserver;

public enum ContentType {

    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css;charset=utf-8"),
    JS("js", "application/javascript"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    ICO("ico", "image/x-icon"),
    SVG("svg", "image/svg+xml");

    private final String extension;
    private final String mimeType;

    ContentType(String extension, String mimeType){
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension(){
        return extension;
    }

    public String getMimeType(){
        return mimeType;
    }

    public static String getContentType(String fileName){ //image.png
        //. 이 파일명에서 마지막으로 등장한 위치를 찾는다
        int dotIndex = fileName.lastIndexOf('.');
        //확장자가 존재하는지 검사 -> .이 존재하고 .다음에 글자가 있어야 함
        if(dotIndex != -1 && dotIndex < fileName.length()-1){
            //fileName에서 파일 확장자 찾기
            String extension = fileName.substring(dotIndex+1).toLowerCase();
            for(ContentType contentType : ContentType.values()){
                if(contentType.getExtension().equals(extension)){
                    return contentType.getMimeType();
                }
            }
        }
        return  "application/octet-stream"; // 기본값 반환
    }




}
