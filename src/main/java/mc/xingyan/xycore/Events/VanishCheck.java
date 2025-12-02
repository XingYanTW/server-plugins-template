package mc.xingyan.xycore.events;

import mc.xingyan.xycore.XyCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static mc.xingyan.xycore.RankManager.getRank;

public class VanishCheck implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        XyCore.getVanish().forEach(player -> {
            if(getRank(event.getPlayer()).equals("DEFAULT")){
                event.getPlayer().hidePlayer(player);
            }
        });

    }


}
