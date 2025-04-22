package request;

import httpconst.HttpConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RequestReader {

    private final BufferedReader bufferedReader;
    private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);

    public RequestReader(InputStream inputStream) throws IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    }

    public RequestStatusLine readStatusLine() throws IOException {
        String statusLine = bufferedReader.readLine();
        logger.debug("Request received: {}", statusLine);

        String[] statusInfo = RequestParser.readStatusLine(statusLine);
        return new RequestStatusLine(statusInfo[0], statusInfo[1], statusInfo[2]);
    }

    public RequestHeader readHeader() throws IOException {
        String request;
        Map<String, String> headers = new HashMap<>();
        while((request = bufferedReader.readLine()) != null && !request.isEmpty()){
            String[] headersInfo = request.split(HttpConst.COLON_SPACE);
            headers.put(headersInfo[0], headersInfo[1]);
            // readheader 메서드 return requestHeader
            logger.debug("Request received: {}", request);
        }
        return new RequestHeader(headers);
    }

    public String readBody(RequestHeader requestHeader) throws IOException {
        String contentLengthValue = requestHeader.getHeaders().get(HttpConst.CONTENT_LENGTH);
        String bodyString = "";

        if (contentLengthValue != null) {
            int contentLength = Integer.parseInt(contentLengthValue);
            char[] body = new char[contentLength];
            int read = bufferedReader.read(body, 0, contentLength);
            bodyString = new String(body, 0, read);
        }

        return bodyString;
    }

}
