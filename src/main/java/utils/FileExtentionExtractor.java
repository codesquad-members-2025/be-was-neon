package utils;

import static constants.SpecialChars.DOT;
import static constants.SpecialChars.EMPTY;

public class FileExtentionExtractor {
    public static String getFileExtension(String filePath) {
        String fileExtension = EMPTY;
        int dotIndex = filePath.lastIndexOf(DOT);
        if (dotIndex > 0) {
            fileExtension = filePath.substring(dotIndex + 1);  // 확장자 추출
        }
        return fileExtension;
    }
}
