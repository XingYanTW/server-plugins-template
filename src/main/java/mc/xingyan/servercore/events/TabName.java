package mc.xingyan.servercore.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import mc.xingyan.servercore.stasis.PlayerInfo;
import mc.xingyan.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static mc.xingyan.servercore.ServerCore.armorStandMap;
import static mc.xingyan.servercore.ServerCore.plugin;

public class TabName implements Listener {

    private final Map<UUID, Integer> tasks = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = PlayerInfo.getPrefix(player);
        player.setPlayerListName(prefix+player.getName());
        Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();



        String weight = "999";
        switch(PlayerInfo.getRank(player)){
            case "ADMIN": weight = "000"; break;
            case "MODERATOR": weight = "001"; break;
            case "YOUTUBER": weight = "002"; break;
            default: weight = "999"; break;
        }

        String teamName = weight + player.getName();
        Team team = score.getTeam(teamName);
        if(team == null) {
            team = score.registerNewTeam(teamName);
        }
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.addEntry(player.getName());

        player.setScoreboard(score);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        int entityId = ThreadLocalRandom.current().nextInt();
        ServerCore.armorStandMap.put(player.getName(), entityId);

        for (Player players : new ArrayList<>(Bukkit.getOnlinePlayers())) {
            if(players.equals(player)) continue;
            sendSpawnPacket(protocolManager, players, player, entityId, prefix + player.getName());
            
            if (armorStandMap.containsKey(players.getName())) {
                int otherId = armorStandMap.get(players.getName());
                sendSpawnPacket(protocolManager, player, players, otherId, PlayerInfo.getPrefix(players) + players.getName());
            }
        }

        int taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                //sendEntityTeleportPacket(protocolManager, player, entityId);
                sendEntityTeleportPacket(player, entityId, player.getLocation());
            }
        },0, 0);
        tasks.put(player.getUniqueId(), taskId);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        
        Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Team team : score.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.unregister();
            }
        }
        
        if (tasks.containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(tasks.get(player.getUniqueId()));
            tasks.remove(player.getUniqueId());
        }
        if (ServerCore.armorStandMap.containsKey(player.getName())) {
            int entityId = ServerCore.armorStandMap.get(player.getName());
            ServerCore.armorStandMap.remove(player.getName());
            
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            PacketContainer destroy = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            destroy.getIntLists().write(0, java.util.Collections.singletonList(entityId));
            
            for (Player players : new ArrayList<>(Bukkit.getOnlinePlayers())) {
                try {
                    protocolManager.sendServerPacket(players, destroy);
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    private void sendSpawnPacket(ProtocolManager pm, Player target, Player source, int entityId, String name) {
        PacketContainer spawn = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawn.getIntegers().write(0, entityId);
        spawn.getUUIDs().write(0, UUID.randomUUID());
        spawn.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        Location loc = source.getLocation();
        spawn.getDoubles().write(0, loc.getX());
        spawn.getDoubles().write(1, loc.getY() - 0.2);
        spawn.getDoubles().write(2, loc.getZ());
        spawn.getBytes().write(0, (byte) (loc.getYaw() * 256.0F / 360.0F));
        spawn.getBytes().write(1, (byte) (loc.getPitch() * 256.0F / 360.0F));

        PacketContainer meta = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        meta.getIntegers().write(0, entityId);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        
        // Invisible (0x20)
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        
        // Custom Name
        Optional<WrappedChatComponent> nameOpt = Optional.of(WrappedChatComponent.fromText(name));
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), nameOpt);
        
        // Custom Name Visible
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        
        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        watcher.getWatchableObjects().stream().forEach(entry -> {
            final WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
            wrappedDataValueList.add(new WrappedDataValue(dataWatcherObject.getIndex(), dataWatcherObject.getSerializer(), entry.getRawValue()));
        });
        meta.getDataValueCollectionModifier().write(0, wrappedDataValueList);
        
        try {
            pm.sendServerPacket(target, spawn);
            pm.sendServerPacket(target, meta);
        } catch (Exception e) { e.printStackTrace(); }
    }

    /*private void sendTeleportPacket(ProtocolManager pm, Player player, int entityId) {
    Location location = player.getLocation();
    double y = location.getY() - 0.2;
    if (player.isSneaking()) {
        y = location.getY() - 0.5;
    }
    
    try {
        PacketContainer teleport = pm.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        teleport.getIntegers().write(0, entityId);
        teleport.getDoubles().write(0, location.getX());
        teleport.getDoubles().write(1, y);
        teleport.getDoubles().write(2, location.getZ());
        teleport.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        teleport.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
        
        // FIX: Check if the boolean field exists before writing
        if (teleport.getBooleans().size() > 0) {
            teleport.getBooleans().write(0, false); // onGround
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            if (!onlinePlayer.getWorld().equals(player.getWorld())) continue;
            try {
                pm.sendServerPacket(onlinePlayer, teleport);
            } catch (Exception e) { e.printStackTrace(); }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }*/

    public void sendEntityTeleportPacket(Player player, int entityId, Location location) {
        double y = location.getY() - 0.2;
        if (player.isSneaking()) {
            y = location.getY() - 0.5;
        }

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        packet.getIntegers().write(0, entityId);

        try {
            InternalStructure is = packet.getStructures().read(0);

            is.getVectors()
               .write(0, new Vector(location.getX(), y, location.getZ()))
               .write(1, new Vector(0, 0, 0));

            is.getFloat()
               .write(0, location.getYaw());
               
            packet.getStructures().write(0, is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            if (!onlinePlayer.getWorld().equals(player.getWorld())) continue;
            try {
                pm.sendServerPacket(onlinePlayer, packet);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}



