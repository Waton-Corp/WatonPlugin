package github.lamorision.watonplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.bukkit.plugin.Plugin;

public class PacketListener extends PacketAdapter {
    ConnectionHandler connections;

    public PacketListener(Plugin plugin, ConnectionHandler connections) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Handshake.Client.SET_PROTOCOL);
        this.connections = connections;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        int protocolVersion = packet.getIntegers().read(0);
        if (protocolVersion == 69420) {
            event.setCancelled(true);
            JsonObject data = getPacketPayload(packet);
            connections.getForPlayer(event.getPlayer()).receive(data);
        }
    }

    public JsonObject getPacketPayload(PacketContainer packet) {
        String strField = packet.getStrings().read(0);
        return Json.parse(strField).asObject();
    }
}
