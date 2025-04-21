package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    private static final int NAME_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    public static HttpRequest parseRequest(InputStream in) throws IOException {
        String[] requestLine;
        List<String> headers;
        String body;

        ByteArrayOutputStream line = new ByteArrayOutputStream();








    }

    public static void readInputStream(InputStream in, ByteArrayOutputStream line, List<String> headers) throws IOException {
        int read;
        boolean isLastCR = false;
        while ((read = in.read()) != -1) {
            if(read=='\r') {
                isLastCR = true;
                continue;
            }
            if(read=='\n' && isLastCR) {
                String singleLine = line.toString(StandardCharsets.UTF_8);
                if(singleLine.isEmpty()) break;
                headers.add(singleLine);
                line.reset();
                isLastCR = false;
                continue;
            }
            line.write(read);
            isLastCR = false;
        }
    }
}

//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//
//        String requestLine = br.readLine();
//        logger.debug("request line : {}", requestLine);
//        String[] requestParts = requestLine.split(" ");
//        String method = requestParts[METHOD_INDEX];
//        String path = requestParts[PATH_INDEX];
//        String version = requestParts[VERSION_INDEX];
//
//        Map<String, String> headers = new HashMap<>();
//        String line;
//        while (!(line = br.readLine()).isEmpty()) {
//            logger.debug("header line : {}", line);
//            String[] headerParts = line.split(": ", 2);
//            headers.put(headerParts[NAME_INDEX], headerParts[VALUE_INDEX]);
//        }
//
//        String body = null;
//        return new HttpRequest(method, path, version, headers, body);




