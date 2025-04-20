package request;

import utils.RequestParser;

public class RequestReader {

    public RequestStatusLine readStatusLine(String statusLine){
        String[] statusInfo = RequestParser.readStatusLine(statusLine);
        return new RequestStatusLine(statusInfo[0], statusInfo[1], statusInfo[2]);
    }

}
