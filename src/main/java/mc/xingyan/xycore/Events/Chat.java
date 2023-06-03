package mc.xingyan.xycore.Events;

import mc.xingyan.xycore.Stasis.playerinfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String prefix = playerinfo.getprefix(player);
        event.setFormat(prefix+player.getDisplayName()+ ChatColor.RESET+": "+event.getMessage());
    }

}
