package server;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class Sender {
    private DataOutputStream out;

    public Sender(Socket clientSocket) {
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String data) throws IOException {
        if (data != null && !data.isEmpty()) {
            out.writeUTF(data);
            out.flush();
        }
    }
}