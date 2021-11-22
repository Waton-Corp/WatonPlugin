package github.lamorision.watonplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.eclipsesource.json.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class Connection {
    Player player;
    String authToken, messageFormat;
    boolean authorized;

    public Connection(Player player, String authToken, String messageFormat) {
        this.player = player;
        this.authToken = authToken;
        this.messageFormat = messageFormat;
    }

    public void receive(JsonObject data) {
        var packetType = data.getString("type", "no_type");
        if (authorized) {
            switch (packetType) {
                case "discord_msg":
                    String sender = data.getString("user", "");
                    String content = data.getString("content", "");
                    var message = new Message(sender, content);
                    Bukkit.broadcastMessage(message.format(messageFormat));
                    break;
            }
        } else {
            if (packetType.equals("handshake")) {
                var token = data.getString("token", "");
                authorized = token.equals(authToken);
                var response = new JsonObject();
                response.add("type", "authorize_response");
                response.add("response", authorized);
                send(response);
                if (!authorized)
                    player.kickPlayer(null);
            }
        }
    }

    public void send(JsonObject data) {
        if (this.isOnline()) {
            ProtocolManager protocol = ProtocolLibrary.getProtocolManager();
            String json = data.toString();
            try {
                protocol.sendWirePacket(player, 0, json.getBytes(StandardCharsets.UTF_8));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public boolean isAuthorized() { return authorized; }
}
