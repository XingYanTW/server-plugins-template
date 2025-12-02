package mc.xingyan.servercore.events;

import mc.xingyan.servercore.stasis.PlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitMessage implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = PlayerInfo.getPrefix(player);

        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a+&7] "+ prefix+player.getName()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        String prefix = PlayerInfo.getPrefix(player);

        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c-&7] "+ prefix+player.getName()));
    }

}

