package Client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.DataInputStream;
import java.io.IOException;

public class AuthenticatedDataInputStream {
    DataInputStream dataInputStream;
    String id;

    public AuthenticatedDataInputStream(DataInputStream dataInputStream, String id) {
        this.dataInputStream = dataInputStream;
        this.id = id;
    }

    public String readUTF() throws IOException {
        String input = dataInputStream.readUTF();
        try {
            AuthenticatingPacket packet = gson().fromJson(input, AuthenticatingPacket.class);
            if (packet.id().equals(this.id)) return packet.str();
            else return "";
        } catch (JsonSyntaxException e) {
            return "";
        }
    }


    private Gson gson = null;

    private Gson gson() {
        if (gson == null) gson = new Gson();
        return gson;
    }
}
