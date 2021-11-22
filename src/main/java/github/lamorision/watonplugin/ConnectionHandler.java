package github.lamorision.watonplugin;

import com.eclipsesource.json.JsonObject;
import org.bukkit.entity.Player;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {
    String authToken, messageFormat;
    ConcurrentHashMap<Player, Connection> connections;

    public ConnectionHandler(String authToken, String messageFormat) {
        this.connections = new ConcurrentHashMap<>();
        this.authToken = authToken;
        this.messageFormat = messageFormat;
    }

    public Connection getForPlayer(Player player) {
        Connection conn = connections.get(player);
        if (conn == null) {
            conn = new Connection(player, authToken, messageFormat);
            connections.put(player, conn);
            cleanClosedConnections();
        }
        return conn;
    }

    public void broadcast(JsonObject data) {
        for (Connection conn: connections.values()) {
            if (conn.isAuthorized())
                conn.send(data);
        }
    }

    void cleanClosedConnections() {
        connections.entrySet().removeIf(entry -> !entry.getValue().isOnline());
    }
}
