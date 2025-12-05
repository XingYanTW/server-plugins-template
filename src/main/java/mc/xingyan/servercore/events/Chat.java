package mc.xingyan.servercore.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import mc.xingyan.servercore.stasis.PlayerInfo;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event){
        Player player = event.getPlayer();
        String prefix = PlayerInfo.getPrefix(player);
        event.renderer((source, sourceDisplayName, message, viewer) -> 
            MiniMessage.miniMessage().deserialize(prefix)
                .append(sourceDisplayName)
                .append(MiniMessage.miniMessage().deserialize("<reset>: "))
                .append(message)
        );
    }

}


