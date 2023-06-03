package mc.xingyan.xycore.Events;

import mc.xingyan.xycore.Stasis.playerinfo;
import mc.xingyan.xycore.main;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static mc.xingyan.xycore.main.armorStandMap;
import static mc.xingyan.xycore.main.plugin;

public class TabName implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = playerinfo.getprefix(player);
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

        switch(playerinfo.getrank(player)){
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

        Location loc = player.getLocation();
        WorldServer ws = ((CraftWorld) player.getWorld()).getHandle();
        EntityArmorStand stand = new EntityArmorStand(ws);
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        stand.setCustomName(prefix+player.getName());
        stand.setCustomNameVisible(true);
        stand.setInvisible(true);
        main.armorStandMap.put(player.getName(), stand);
        PlayerConnection playerc = ((CraftPlayer) player).getHandle().playerConnection;
        Bukkit.getOnlinePlayers().forEach(players -> {
            if(players.equals(player)) return;
            PlayerConnection cp = ((CraftPlayer) players).getHandle().playerConnection;
            cp.sendPacket(new PacketPlayOutSpawnEntityLiving(stand));
            EntityArmorStand os = armorStandMap.get(players.getName());
            playerc.sendPacket(new PacketPlayOutSpawnEntityLiving(os));
        });
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                Location location = player.getLocation();
                if(player.isSneaking()){
                    stand.setLocation(location.getX(), location.getY()-0.5, location.getZ(), location.getYaw(), location.getPitch());
                    stand.setSneaking(true);
                }else{
                    stand.setLocation(location.getX(), location.getY()-0.2, location.getZ(), location.getYaw(), location.getPitch());
                    stand.setSneaking(false);
                }
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if(players.equals(player)) return;
                    PlayerConnection cp = ((CraftPlayer) players).getHandle().playerConnection;
                    cp.sendPacket(new PacketPlayOutEntityTeleport(stand));
                });
            }
        },0, 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        EntityArmorStand stand = main.armorStandMap.get(player.getName());
        main.armorStandMap.remove(player.getName());
        Bukkit.getOnlinePlayers().forEach(players -> {
            PlayerConnection cp = ((CraftPlayer) players).getHandle().playerConnection;
            cp.sendPacket(new PacketPlayOutEntityDestroy(stand.getId()));
        });

    }



}
