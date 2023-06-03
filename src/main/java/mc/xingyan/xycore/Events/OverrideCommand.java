package mc.xingyan.xycore.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.List;

public class OverrideCommand implements Listener {

    @EventHandler
    public void kickcommand(PlayerCommandPreprocessEvent event){
        if(event.getMessage().startsWith("/kick")){
            event.setCancelled(true);
            String newcommand = event.getMessage().replace("/kick", "xycore:kick");
            event.getPlayer().performCommand(newcommand);
        }
    }

    @EventHandler
    public void consolekick(ServerCommandEvent event){
        if(event.getCommand().startsWith("kick")){
            event.setCancelled(true);
            String newcommand = event.getCommand().replace("kick", "xycore:kick");
            Bukkit.getServer().dispatchCommand(event.getSender(), newcommand);
        }
    }

    @EventHandler
    public void bancommand(PlayerCommandPreprocessEvent event){
        if(event.getMessage().startsWith("/ban")){
            event.setCancelled(true);
            String newcommand = event.getMessage().replace("/ban", "xycore:ban");
            event.getPlayer().performCommand(newcommand);
        }
    }

    @EventHandler
    public void consoleban(ServerCommandEvent event){
        if(event.getCommand().startsWith("ban")){
            event.setCancelled(true);
            String newcommand = event.getCommand().replace("ban", "xycore:ban");
            Bukkit.getServer().dispatchCommand(event.getSender(), newcommand);
        }
    }

}
