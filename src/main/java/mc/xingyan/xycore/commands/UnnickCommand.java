package mc.xingyan.xycore.commands;

import mc.xingyan.xycore.stasis.ChangeNick;
import mc.xingyan.xycore.XyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.xycore.RankManager.getRank;


public class UnnickCommand extends XyCommand {

    @Override
    public String getName() {
        return "unnick";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getRank(player).equals("YOUTUBER") || getRank(player).equals("MODERATOR") || getRank(player).equals("ADMIN")){
                if(XyCore.getNicked().contains(player)){
                    XyCore.getNicked().remove(player);
                    System.out.println(XyCore.realname.get(player));
                    player.setDisplayName(XyCore.realname.get(player));
                    player.setPlayerListName(XyCore.realname.get(player));

                    new ChangeNick(XyCore.realname.get(player), player);

                    player.sendMessage(ChatColor.GREEN+"You Has Removed Your Nickname");
                }else{
                    sender.sendMessage(ChatColor.RED+"You Aren't Nicked");
                }
            }


        }
        return true;
    }


}
