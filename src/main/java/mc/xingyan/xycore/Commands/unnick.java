package mc.xingyan.xycore.Commands;

import mc.xingyan.xycore.Stasis.changeNick;
import mc.xingyan.xycore.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.xycore.getrank.getrank;


public class unnick implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getrank(player).equals("YOUTUBER") || getrank(player).equals("MODERATOR") || getrank(player).equals("ADMIN")){
                if(main.getNicked().contains(player)){
                    main.getNicked().remove(player);
                    System.out.println(main.realname.get(player));
                    player.setDisplayName(main.realname.get(player));
                    player.setPlayerListName(main.realname.get(player));

                    new changeNick(main.realname.get(player), player);

                    player.sendMessage(ChatColor.GREEN+"You Has Removed Your Nickname");
                }else{
                    sender.sendMessage(ChatColor.RED+"You Aren't Nicked");
                }
            }


        }
        return true;
    }


}
