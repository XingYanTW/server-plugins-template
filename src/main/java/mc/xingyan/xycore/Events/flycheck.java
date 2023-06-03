package mc.xingyan.xycore.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static mc.xingyan.xycore.getrank.getrank;

public class flycheck implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(getrank(player).equals("YOUTUBER") || getrank(player).equals("MODERATOR") || getrank(player).equals("ADMIN")){
            player.setAllowFlight(true);
        }
    }
}
