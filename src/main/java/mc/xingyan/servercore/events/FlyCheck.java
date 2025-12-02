package mc.xingyan.servercore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static mc.xingyan.ServerCore.RankManager.getRank;

public class FlyCheck implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(getRank(player).equals("YOUTUBER") || getRank(player).equals("MODERATOR") || getRank(player).equals("ADMIN")){
            player.setAllowFlight(true);
        }
    }
}

