package utils;

public class FileExtentionExtractor {
    public static String getFileExtension(String filePath) {
        String fileExtension = "";
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = filePath.substring(dotIndex + 1);  // 확장자 추출
        }
        return fileExtension;
    }
}
