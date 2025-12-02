package mc.xingyan.xycore.commands;

import mc.xingyan.xycore.XyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.xycore.RankManager.getRank;

public class VanishCommand extends XyCommand {

    @Override
    public String getName() {
        return "vanish";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getRank(player).equals("YOUTUBER") || getRank(player).equals("MODERATOR") || getRank(player).equals("ADMIN")){
                if(!XyCore.getVanish().contains(player)){
                    Bukkit.getOnlinePlayers().forEach(p->{
                        if(getRank(p).equals("DEFAULT")){
                            p.hidePlayer(player);
                        }
                    });
                    XyCore.getVanish().add(player);
                    player.sendMessage(ChatColor.GREEN+"You Are Now Vanish From Other Player.");
                }else{
                    Bukkit.getOnlinePlayers().forEach(p->{
                        p.showPlayer(player);
                    });
                    XyCore.getVanish().remove(player);
                    player.sendMessage(ChatColor.GREEN+"You Are Now Visible From Other Player.");
                }
            }else{
                sender.sendMessage(ChatColor.RED+"You Need YOUTUBER rank or higher to do this.");
            }

        }

        return true;
    }
}
