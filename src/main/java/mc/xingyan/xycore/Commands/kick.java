package mc.xingyan.xycore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static mc.xingyan.xycore.getrank.getrank;

public class kick implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getrank(player).equals("MODERATOR") || getrank(player).equals("ADMIN")){
                if(args.length>=1){
                    if(Bukkit.getPlayer(args[0]) !=null ){
                        Player target = Bukkit.getPlayer(args[0]);
                        if(args.length>=2){
                            String reason = "";
                            for(int i=1; i<=args.length-1; i++){
                                reason += args[i] + " ";
                            }
                            target.kickPlayer(ChatColor.translateAlternateColorCodes('&', ChatColor.RED+"You has been kicked from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+reason+"\n\n"+ChatColor.GRAY+sdf.format(timestamp)));
                            Bukkit.getServer().broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"[Notify] "+target.getName()+" Has Been Kicked by Moderator. Reason: "+ChatColor.RESET+reason);
                        }else{
                            target.kickPlayer(ChatColor.RED+"You has been kicked from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+"Kicked\n\n"+ChatColor.GRAY+sdf.format(timestamp));
                            Bukkit.getServer().broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"[Notify] "+target.getName()+" Has Been Kicked by Moderator. Reason: "+ChatColor.RESET+"Kicked");
                        }
                    }
                }else{
                    player.sendMessage(ChatColor.RED+"Usage: /kick <player> [<reason>]");
                }
            }else {
                sender.sendMessage(ChatColor.RED+"You don't have permission to run that command!");
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
                        target.kickPlayer(ChatColor.translateAlternateColorCodes('&', ChatColor.RED+"You has been kicked from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+reason+"\n\n"+ChatColor.GRAY+sdf.format(timestamp)));
                        Bukkit.getServer().broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"[Notify] "+target.getName()+" Has Been Kicked by Console. Reason: "+ChatColor.RESET+reason);
                    }else{
                        target.kickPlayer(ChatColor.RED+"You has been kicked from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+"Kicked\n\n"+ChatColor.GRAY+sdf.format(timestamp));
                        Bukkit.getServer().broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"[Notify] "+target.getName()+" Has Been Kicked by Console. Reason: "+ChatColor.RESET+"Kicked");
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED+"Usage: /kick <player> [<reason>]");
            }
        }
        return true;
    }
}
