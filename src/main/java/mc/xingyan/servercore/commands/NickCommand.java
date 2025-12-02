package mc.xingyan.servercore.commands;

import mc.xingyan.servercore.stasis.ChangeNick;
import mc.xingyan.servercore.ServerCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.servercore.RankManager.getRank;


public class NickCommand extends CoreCommand {

    @Override
    public String getName() {
        return "nick";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getRank(player).equals("YOUTUBER") || getRank(player).equals("MODERATOR") || getRank(player).equals("ADMIN")){
                if(ServerCore.getNicked().contains(player)){
                    player.sendMessage(ChatColor.RED+"You Are Already Nicked");
                    return true;
                }
                if(args.length>=1){
                    ServerCore.realname.put(player, player.getName());
                    System.out.println(ServerCore.realname.get(player));
                    if(args[0].length() <=16){
                        player.setDisplayName(args[0]);
                        player.setPlayerListName(args[0]);

                        new ChangeNick(args[0], player);

                        ServerCore.getNicked().add(player);

                        player.sendMessage(ChatColor.GREEN+"You Has Changed Your Nickname To "+args[0]);
                    }else{
                        player.sendMessage(ChatColor.RED+"Nickname can't longer than 16 charters.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED+"Usage: /nick <nickname>");
                }
            }else{
                sender.sendMessage(ChatColor.RED+"You Need YOUTUBER rank or higher to do this.");
            }
        }

        return true;
    }
}


