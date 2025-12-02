package mc.xingyan.xycore.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import mc.xingyan.xycore.stasis.PlayerInfo;
import mc.xingyan.xycore.XyCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static mc.xingyan.xycore.XyCore.armorStandMap;
import static mc.xingyan.xycore.XyCore.plugin;

public class TabName implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = PlayerInfo.getPrefix(player);
        player.setPlayerListName(prefix+player.getName());
        Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();



        Team admin = score.getTeam("000admin");
        Team mod = score.getTeam("001mod");
        Team yt = score.getTeam("002yt");
        Team def = score.getTeam("999def");
        if(admin == null) {
            admin = score.registerNewTeam("000admin");
            admin.setNameTagVisibility(NameTagVisibility.NEVER);
        }
        if(mod == null) {
            mod = score.registerNewTeam("001mod");
            mod.setNameTagVisibility(NameTagVisibility.NEVER);
        }
        if(yt == null) {
            yt = score.registerNewTeam("002yt");
            yt.setNameTagVisibility(NameTagVisibility.NEVER);
        }
        if(def == null) {
            def = score.registerNewTeam("999def");
        }
        admin.setNameTagVisibility(NameTagVisibility.NEVER);
        mod.setNameTagVisibility(NameTagVisibility.NEVER);
        yt.setNameTagVisibility(NameTagVisibility.NEVER);
        def.setNameTagVisibility(NameTagVisibility.NEVER);

        switch(PlayerInfo.getRank(player)){
            case "DEFAULT":
                def.addEntry(player.getName());
                break;
            case "ADMIN":
                admin.addEntry(player.getName());
                break;
            case "YOUTUBER":
                yt.addEntry(player.getName());
                break;
            case "MODERATOR":
                mod.addEntry(player.getName());
                break;
        }
        //t.addEntry(player.getName());
        player.setScoreboard(score);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        int entityId = ThreadLocalRandom.current().nextInt();
        XyCore.armorStandMap.put(player.getName(), entityId);

        Bukkit.getOnlinePlayers().forEach(players -> {
            if(players.equals(player)) return;
            sendSpawnPacket(protocolManager, players, player, entityId, prefix + player.getName());
            
            if (armorStandMap.containsKey(players.getName())) {
                int otherId = armorStandMap.get(players.getName());
                sendSpawnPacket(protocolManager, player, players, otherId, PlayerInfo.getPrefix(players) + players.getName());
            }
        });

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                Location location = player.getLocation();
                double y = location.getY() - 0.2;
                if(player.isSneaking()){
                    y = location.getY() - 0.5;
                }
                
                PacketContainer teleport = protocolManager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
                teleport.getIntegers().write(0, entityId);
                teleport.getDoubles().write(0, location.getX());
                teleport.getDoubles().write(1, y);
                teleport.getDoubles().write(2, location.getZ());
                teleport.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
                teleport.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
                teleport.getBooleans().write(0, false); // onGround

                Bukkit.getOnlinePlayers().forEach(players -> {
                    if(players.equals(player)) return;
                    try {
                        protocolManager.sendServerPacket(players, teleport);
                    } catch (Exception e) { e.printStackTrace(); }
                });
            }
        },0, 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (XyCore.armorStandMap.containsKey(player.getName())) {
            int entityId = XyCore.armorStandMap.get(player.getName());
            XyCore.armorStandMap.remove(player.getName());
            
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            PacketContainer destroy = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            destroy.getIntLists().write(0, java.util.Collections.singletonList(entityId));
            
            Bukkit.getOnlinePlayers().forEach(players -> {
                try {
                    protocolManager.sendServerPacket(players, destroy);
                } catch (Exception e) { e.printStackTrace(); }
            });
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
        
        meta.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        
        try {
            pm.sendServerPacket(target, spawn);
            pm.sendServerPacket(target, meta);
        } catch (Exception e) { e.printStackTrace(); }
    }

}
