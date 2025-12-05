package mc.xingyan.servercore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static mc.xingyan.servercore.RankManager.getRank;

public class KickCommand extends CoreCommand {

    @Override
    public String getName() {
        return "kick";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getRank(player).equals("MODERATOR") || getRank(player).equals("ADMIN")){
                if(args.length>=1){
                    if(Bukkit.getPlayer(args[0]) !=null ){
                        Player target = Bukkit.getPlayer(args[0]);
                        if(args.length>=2){
                            String reason = "";
                            for(int i=1; i<=args.length-1; i++){
                                reason += args[i] + " ";
                            }
                            String kickMsg = "<red>You has been kicked from this server!\n\n<gray>Reason: <white>" + reason + "\n\n<gray>" + sdf.format(timestamp);
                            target.kick(MiniMessage.miniMessage().deserialize(kickMsg));
                            Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize("<red><bold>[Notify] " + target.getName() + " Has Been Kicked by Moderator. Reason: <reset>" + reason));
                        }else{
                            String kickMsg = "<red>You has been kicked from this server!\n\n<gray>Reason: <white>Kicked\n\n<gray>" + sdf.format(timestamp);
                            target.kick(MiniMessage.miniMessage().deserialize(kickMsg));
                            Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize("<red><bold>[Notify] " + target.getName() + " Has Been Kicked by Moderator. Reason: <reset>Kicked"));
                        }
                    }
                }else{
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /kick <player> [<reason>]"));
                }
            }else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You don't have permission to run that command!"));
            }
        }
        if(sender instanceof ConsoleCommandSender){
            if(args.length>=1){
                if(Bukkit.getPlayer(args[0]) !=null ){
                    Player target = Bukkit.getPlayer(args[0]);
                    if(args.length>=2){
                        String reason = "";
                        for(int i=1; i<=args.length-1; i++){
                            reason += args[i] + " ";
                        }
                        String kickMsg = "<red>You has been kicked from this server!\n\n<gray>Reason: <white>" + reason + "\n\n<gray>" + sdf.format(timestamp);
                        target.kick(MiniMessage.miniMessage().deserialize(kickMsg));
                        Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize("<red><bold>[Notify] " + target.getName() + " Has Been Kicked by Console. Reason: <reset>" + reason));
                    }else{
                        String kickMsg = "<red>You have been kicked from this server!\n\n<gray>Reason: <white>Kicked\n\n<gray>" + sdf.format(timestamp);
                        target.kick(MiniMessage.miniMessage().deserialize(kickMsg));
                        Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize("<red><bold>[Notify] " + target.getName() + " Has Been Kicked by Console. Reason: <reset>Kicked"));
                    }
                }
            }else{
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /kick <player> [<reason>]"));
            }
        }
        return true;
    }
}


