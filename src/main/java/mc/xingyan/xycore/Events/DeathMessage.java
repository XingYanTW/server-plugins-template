package mc.xingyan.xycore.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessage implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.setDeathMessage("");
    }

}
