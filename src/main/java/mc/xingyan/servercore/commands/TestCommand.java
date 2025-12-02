package mc.xingyan.servercore.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import mc.xingyan.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestCommand extends CoreCommand {

    @Override
    public String getName() {
        return "test";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        if (args[0].equals("spawn")) {

            Location loc = player.getLocation();
            int entityId = ThreadLocalRandom.current().nextInt();
            ServerCore.armorStandMap.put(player.getName(), entityId);

            Bukkit.getOnlinePlayers().forEach(players -> {
                PacketContainer spawn = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
                spawn.getIntegers().write(0, entityId);
                spawn.getUUIDs().write(0, UUID.randomUUID());
                spawn.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
                spawn.getDoubles().write(0, loc.getX());
                spawn.getDoubles().write(1, loc.getY());
                spawn.getDoubles().write(2, loc.getZ());
                spawn.getBytes().write(0, (byte) (loc.getYaw() * 256.0F / 360.0F));
                spawn.getBytes().write(1, (byte) (loc.getPitch() * 256.0F / 360.0F));

                PacketContainer meta = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
                meta.getIntegers().write(0, entityId);
                WrappedDataWatcher watcher = new WrappedDataWatcher();
                
                // Invisible (0x20)
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
                
                // Custom Name
                Optional<WrappedChatComponent> nameOpt = Optional.of(WrappedChatComponent.fromText(player.getName()));
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), nameOpt);
                
                // Custom Name Visible
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
                
                meta.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

                try {
                    protocolManager.sendServerPacket(players, spawn);
                    protocolManager.sendServerPacket(players, meta);
                } catch (Exception e) { e.printStackTrace(); }
            });
        }
        if(args[0].equals("kill")){
            if (ServerCore.armorStandMap.containsKey(player.getName())) {
                int entityId = ServerCore.armorStandMap.get(player.getName());
                Bukkit.getOnlinePlayers().forEach(players -> {
                    PacketContainer destroy = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
                    destroy.getIntLists().write(0, java.util.Collections.singletonList(entityId));
                    try {
                        protocolManager.sendServerPacket(players, destroy);
                    } catch (Exception e) { e.printStackTrace(); }
                });
            }
        }


        return true;
    }
}


