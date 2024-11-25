package naver.et0709.awss3;

public class CommonUtils {
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String CATEGORY_PREFIX = "/";
    private static final String TIME_SEPARATOR = "_";

    //파일의 이름을 만들어주는 메소드를 가진 Class
    public static String buildFileName(String category, String originalFileName) {
        //원본 파일 경로에서 .의 마지막 위치를 찾아낸다
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        //파일 확장자를 추출한다
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        //파일 이름을 추출한다
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        //현재 시간을 기록한다
        String now = String.valueOf(System.currentTimeMillis());

        return category + CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now
                + fileExtension;
    }
}