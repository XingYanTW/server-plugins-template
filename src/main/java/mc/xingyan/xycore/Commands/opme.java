package mc.xingyan.xycore.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.xycore.getrank.getrank;

public class opme implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getrank(player).equals("ADMIN")){
                if(player.isOp()){
                    player.setOp(false);
                    player.sendMessage(ChatColor.RED+ "You are no longer OP.");
                }else{
                    player.setOp(true);
                    player.sendMessage(ChatColor.GREEN+"You are now OP.");
                }
            }
        }

        return true;
    }
}
