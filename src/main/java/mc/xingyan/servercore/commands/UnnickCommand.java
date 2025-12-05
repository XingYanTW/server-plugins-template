package mc.xingyan.servercore.commands;

import mc.xingyan.servercore.stasis.ChangeNick;
import mc.xingyan.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static mc.xingyan.servercore.RankManager.getRank;


public class UnnickCommand extends CoreCommand {

    @Override
    public String getName() {
        return "unnick";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getRank(player).equals("YOUTUBER") || getRank(player).equals("MODERATOR") || getRank(player).equals("ADMIN")){
                if(ServerCore.getNicked().contains(player)){
                    ServerCore.getNicked().remove(player);
                    System.out.println(ServerCore.realname.get(player));
                    player.displayName(MiniMessage.miniMessage().deserialize(ServerCore.realname.get(player)));
                    player.playerListName(MiniMessage.miniMessage().deserialize(ServerCore.realname.get(player)));

                    new ChangeNick(ServerCore.realname.get(player), player);

                    player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You Has Removed Your Nickname"));
                }else{
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You Aren't Nicked"));
                }
            }


        }
        return true;
    }


}


