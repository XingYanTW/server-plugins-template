package mc.xingyan.servercore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import mc.xingyan.servercore.ServerCore;

import static mc.xingyan.servercore.RankManager.getRank;

public class BanCommand extends CoreCommand {

    @Override
    public String getName() {
        return "ban";
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
                            String banMsg = "<red>You has been banned from this server!\n\n<gray>Reason: <white>" + reason + "\n\n<gray>" + sdf.format(timestamp);
                            target.kick(MiniMessage.miniMessage().deserialize(banMsg));
                            banplayer(target, banMsg);
                            Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize("<red><bold>[Notify] " + target.getName() + " Has Been Banned by Moderator. Reason: <reset>" + reason));
                        }else{
                            String banMsg = "<red>You has been banned from this server!\n\n<gray>Reason: <white>Banned\n\n<gray>" + sdf.format(timestamp);
                            target.kick(MiniMessage.miniMessage().deserialize(banMsg));
                            banplayer(target, banMsg);
                            Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize("<red><bold>[Notify] " + target.getName() + " Has Been Banned by Moderator. Reason: <reset>Banned"));
                        }
                    }
                }else{
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /ban <player> [<reason>]"));
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
                        String banMsg = "<red>You has been banned from this server!\n\n<gray>Reason: <white>" + reason + "\n\n<gray>" + sdf.format(timestamp);
                        target.kick(MiniMessage.miniMessage().deserialize(banMsg));
                        banplayer(target, banMsg);
                        Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize("<red><bold>[Notify] " + target.getName() + " Has Been Banned by Moderator. Reason: <reset>" + reason));
                    }else{
                        String banMsg = "<red>You have been banned from this server!\n\n<gray>Reason: <white>Banned\n\n<gray>" + sdf.format(timestamp);
                        target.kick(MiniMessage.miniMessage().deserialize(banMsg));
                        banplayer(target, banMsg);
                        Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize("<red><bold>[Notify] " + target.getName() + " Has Been Banned by Moderator. Reason: <reset>Banned"));
                    }
                }
            }else{
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /ban <player> [<reason>]"));
            }
        }
        return true;
    }

    private static String banplayer(Player player, String reason){
        try (PreparedStatement checkStmt = ServerCore.connection.prepareStatement("SELECT * FROM Ban WHERE PlayerUUID = ?")) {
            checkStmt.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = checkStmt.executeQuery()) {
                if (resultSet.next()) {
                    return "ALREADY";
                }
            }
            
            try (PreparedStatement insertStmt = ServerCore.connection.prepareStatement("INSERT INTO Ban (PlayerUUID, PlayerID, BannedReason) VALUES (?, ?, ?)")) {
                insertStmt.setString(1, player.getUniqueId().toString());
                insertStmt.setString(2, player.getName());
                insertStmt.setString(3, reason);
                insertStmt.executeUpdate();
                System.out.println("Success! Inserted ban for: " + player.getName());
                return "SUCCESSFUL";
            }
        } catch (SQLException e) {
            System.err.println("Unable to insert due to an error: " + e);
        }
        return null;
    }


}


