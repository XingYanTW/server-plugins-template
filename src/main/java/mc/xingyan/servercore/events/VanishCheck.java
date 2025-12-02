package mc.xingyan.servercore.events;

import mc.xingyan.servercore.ServerCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static mc.xingyan.ServerCore.RankManager.getRank;

public class VanishCheck implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        ServerCore.getVanish().forEach(player -> {
            if(getRank(event.getPlayer()).equals("DEFAULT")){
                event.getPlayer().hidePlayer(player);
            }
        });

    }


}

