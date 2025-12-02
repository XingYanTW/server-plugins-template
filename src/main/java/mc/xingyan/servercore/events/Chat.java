package mc.xingyan.servercore.events;

import mc.xingyan.servercore.stasis.PlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String prefix = PlayerInfo.getPrefix(player);
        event.setFormat(prefix+player.getDisplayName()+ ChatColor.RESET+": "+event.getMessage());
    }

}


