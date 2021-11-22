package github.lamorision.watonplugin;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameEventsListener implements Listener {
    ConnectionHandler connections;

    public GameEventsListener(ConnectionHandler connections) {
        this.connections = connections;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        JsonObject data = new JsonObject();
        data.add("type", "minecraft_msg");
        data.add("user", event.getPlayer().getName());
        data.add("content", event.getMessage());
        connections.broadcast(data);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        JsonObject data = new JsonObject();
        data.add("type", "player_join");
        data.add("user", event.getPlayer().getName());
        JsonArray list = new JsonArray();
        for (Player player: Bukkit.getOnlinePlayers()) {
            list.add(player.getName());
        }
        data.add("player_list", list);
        connections.broadcast(data);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLeave(PlayerQuitEvent event) {
        JsonObject data = new JsonObject();
        data.add("type", "player_leave");
        data.add("user", event.getPlayer().getName());
        JsonArray list = new JsonArray();
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (!player.equals(event.getPlayer()))
                list.add(player.getName());
        }
        data.add("player_list", list);
        connections.broadcast(data);
    }
}
