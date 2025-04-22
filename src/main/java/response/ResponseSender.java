package response;

import Exceptions.HttpException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseSender {
    private final DataOutputStream dos;

    public ResponseSender(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void send(Response response) {
        try{
            byte[] body = response.getBody();
            dos.writeBytes(response.getHeader());
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, "응답 전송 실패");
        }
    }
}
