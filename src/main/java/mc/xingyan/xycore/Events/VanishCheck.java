package mc.xingyan.xycore.Events;

import mc.xingyan.xycore.main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static mc.xingyan.xycore.getrank.getrank;

public class VanishCheck implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        main.getVanish().forEach(player -> {
            if(getrank(event.getPlayer()).equals("DEFAULT")){
                event.getPlayer().hidePlayer(player);
            }
        });

    }


}
