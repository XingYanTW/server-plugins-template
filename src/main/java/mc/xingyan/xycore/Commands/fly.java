package mc.xingyan.xycore.Commands;

import mc.xingyan.xycore.Stasis.changeNick;
import mc.xingyan.xycore.main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.xycore.getrank.getrank;

public class fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getrank(player).equals("YOUTUBER") || getrank(player).equals("MODERATOR") || getrank(player).equals("ADMIN")){
                if(player.getAllowFlight()){
                    player.setAllowFlight(false);
                    player.sendMessage(ChatColor.RED+"Flying Disabled");
                    return true;
                }
                if(!player.getAllowFlight()){
                    player.setAllowFlight(true);
                    player.sendMessage(ChatColor.GREEN+"Flying Enabled");
                    return true;
                }
            }
        }
        return true;
    }
}
