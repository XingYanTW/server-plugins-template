package mc.xingyan.xycore.Commands;

import mc.xingyan.xycore.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.xycore.getrank.getrank;

public class vanish implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getrank(player).equals("YOUTUBER") || getrank(player).equals("MODERATOR") || getrank(player).equals("ADMIN")){
                if(!main.getVanish().contains(player)){
                    Bukkit.getOnlinePlayers().forEach(p->{
                        if(getrank(p).equals("DEFAULT")){
                            p.hidePlayer(player);
                        }
                    });
                    main.getVanish().add(player);
                    player.sendMessage(ChatColor.GREEN+"You Are Now Vanish From Other Player.");
                }else{
                    Bukkit.getOnlinePlayers().forEach(p->{
                        p.showPlayer(player);
                    });
                    main.getVanish().remove(player);
                    player.sendMessage(ChatColor.GREEN+"You Are Now Visible From Other Player.");
                }
            }else{
                sender.sendMessage(ChatColor.RED+"You Need YOUTUBER rank or higher to do this.");
            }

        }

        return true;
    }
}
