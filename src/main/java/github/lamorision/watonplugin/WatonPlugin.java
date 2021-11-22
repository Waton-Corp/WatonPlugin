package github.lamorision.watonplugin;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.SecureRandom;
import java.util.Base64;

public class WatonPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        var authToken = getConfig().getString("auth-token");
        if (authToken == null) {
            var random = new SecureRandom();
            byte bytes[] = new byte[9];
            random.nextBytes(bytes);
            authToken = Base64.getEncoder().encodeToString(bytes);
            getConfig().set("auth-token", authToken);
            saveConfig();
        }
        var messageFormat = getConfig().getString("message-format");
        var connections = new ConnectionHandler(authToken, messageFormat);
        var packets = new PacketListener(this, connections);
        ProtocolLibrary.getProtocolManager().addPacketListener(packets);
        var gameEvents = new GameEventsListener(connections);
        getServer().getPluginManager().registerEvents(gameEvents, this);
    }
}
