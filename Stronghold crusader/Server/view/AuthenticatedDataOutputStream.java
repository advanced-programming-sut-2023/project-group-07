package Server.view;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.DataOutputStream;
import java.io.IOException;

public class AuthenticatedDataOutputStream {
    private DataOutputStream dataOutputStream;
    private String id;

    public AuthenticatedDataOutputStream(DataOutputStream dataOutputStream, String id) {
        this.dataOutputStream = dataOutputStream;
        this.id = id;
    }

    public void writeUTF(String str) throws IOException {
        AuthenticatingPacket packet = new AuthenticatingPacket(str, this.id);
        String packetInGson = gson().toJson(packet);
        dataOutputStream.writeUTF(packetInGson);
    }

    private Gson gson = null;

    private Gson gson() {
        if (gson == null) gson = new Gson();
        return gson;
    }
}
