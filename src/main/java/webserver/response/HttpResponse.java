package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private final DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void response200(byte[] body, String contentType) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: "+ contentType+"\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }


    public void response404() throws IOException {
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n\r\n");
    }

    public void sendRedirect(String location) {

        try {
            //HTTP/1.1 302 Found
            //Location: http://www.iana.org/domains/example/
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: "+location+"\r\n");
        }catch (IOException e){
            //todo : Logger로..
            e.printStackTrace();
        }

    }
}
