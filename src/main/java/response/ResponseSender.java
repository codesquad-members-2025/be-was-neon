package response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseSender {
    private final DataOutputStream dos;

    public ResponseSender(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void send(Response response) throws IOException {
        byte[] body = response.getBody();
        dos.writeBytes(response.getHeader());
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
