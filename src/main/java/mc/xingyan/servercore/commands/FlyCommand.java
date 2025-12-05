package mc.xingyan.servercore.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.servercore.RankManager.getRank;

public class FlyCommand extends CoreCommand {

    @Override
    public String getName() {
        return "fly";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getRank(player).equals("YOUTUBER") || getRank(player).equals("MODERATOR") || getRank(player).equals("ADMIN")){
                if(player.getAllowFlight()){
                    player.setAllowFlight(false);
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Flying Disabled"));
                    return true;
                }
                if(!player.getAllowFlight()){
                    player.setAllowFlight(true);
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Flying Enabled"));
                    return true;
                }
            }
        }
        return true;
    }
}


