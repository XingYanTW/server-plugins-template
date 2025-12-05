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
import java.util.Locale;
import mc.xingyan.servercore.ServerCore;

public class SetRankCommand extends CoreCommand {

    @Override
    public String getName() {
        return "setrank";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender){
            if(args.length>=2){
                if(Bukkit.getPlayer(args[0]) !=null){
                    Player player = Bukkit.getPlayer(args[0]);
                    if(checkrank(args[1])){
                        try (PreparedStatement statement = ServerCore.connection.prepareStatement("UPDATE Ranks SET PlayerRank = ? WHERE PlayerUUID = ?")) {
                            statement.setString(1, args[1].toUpperCase(Locale.ROOT));
                            statement.setString(2, player.getUniqueId().toString());
                            int rows = statement.executeUpdate();
                            if (rows > 0) {
                                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You Are Now "+args[1].toUpperCase(Locale.ROOT)));
                                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successful Set "+player.getName()+"'s Rank to "+ args[1].toUpperCase(Locale.ROOT)));
                            } else {
                                // If not found, maybe insert? Or just say not found. The original code only updated if found.
                                // But usually setrank should work even if not found (insert).
                                // Original code: if(found !=null) ...
                                // I'll stick to original logic, but maybe I should insert if not exists?
                                // Let's check if it exists first to match original logic, or use INSERT OR REPLACE / UPSERT logic.
                                // Original code only updated if found.
                                
                                // Let's try to insert if update failed (0 rows), assuming the player should have a rank.
                                // But wait, PlayerFirstJoin probably creates the default rank.
                                // If the player is online (Bukkit.getPlayer != null), they should have a rank entry if PlayerFirstJoin works.
                                
                                // Let's just stick to update.
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else{
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Rank Not Found!"));
                    }
                } else {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Player Not Found!"));
                }
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /setrank <player> <rank>"));
            }

        }
        if(sender instanceof Player){
            Player p = (Player) sender;
            try (PreparedStatement statement = ServerCore.connection.prepareStatement("SELECT PlayerRank FROM Ranks WHERE PlayerUUID = ?")) {
                statement.setString(1, p.getUniqueId().toString());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        if(resultSet.getString("PlayerRank").equals("ADMIN")){
                            if(args.length>=2){
                                if(Bukkit.getPlayer(args[0]) !=null){
                                    Player player = Bukkit.getPlayer(args[0]);
                                    if(checkrank(args[1])){
                                        try (PreparedStatement updateStmt = ServerCore.connection.prepareStatement("UPDATE Ranks SET PlayerRank = ? WHERE PlayerUUID = ?")) {
                                            updateStmt.setString(1, args[1].toUpperCase(Locale.ROOT));
                                            updateStmt.setString(2, player.getUniqueId().toString());
                                            updateStmt.executeUpdate();
                                            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You Are Now "+args[1].toUpperCase(Locale.ROOT)));
                                            p.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successful Set "+player.getName()+"'s Rank to "+ args[1].toUpperCase(Locale.ROOT)));
                                        }
                                    }else{
                                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Rank Not Found!"));
                                    }
                                } else {
                                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Player Not Found!"));
                                }
                            } else {
                                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /setrank <player> <rank>"));
                            }
                        }else{
                            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You don't have permission to run that command!"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    private static boolean checkrank(String rank){
        switch (rank.toUpperCase(Locale.ROOT)){
            case "ADMIN":
            case "MODERATOR":
            case "YOUTUBER":
            case "DEFAULT":
                return true;
            default:
                return false;
        }
    }
}


