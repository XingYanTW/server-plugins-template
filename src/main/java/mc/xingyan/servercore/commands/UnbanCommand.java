package mc.xingyan.servercore.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mc.xingyan.servercore.ServerCore;

import static mc.xingyan.servercore.RankManager.getRank;

public class UnbanCommand extends CoreCommand {

    @Override
    public String getName() {
        return "unban";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            if(args.length>=1){
                if(checkban(args[0])){
                    removeban(args[0]);
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successful Unbanned Player!"));
                }else{
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Player Not Found."));
                }
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /unban <player>"));
            }
        }
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (getRank(player).equals("MODERATOR") || getRank(player).equals("ADMIN")) {
                if(args.length>=1){
                    if(checkban(args[0])){
                        removeban(args[0]);
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Successful Unbanned Player!"));
                    }else{
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Player Not Found."));
                    }
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /unban <player>"));
                }
            }
        }

        return true;
    }

    private static boolean checkban(String player){
        try (PreparedStatement statement = ServerCore.connection.prepareStatement("SELECT * FROM Ban WHERE PlayerID = ?")) {
            statement.setString(1, player);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void removeban(String player){
        try (PreparedStatement statement = ServerCore.connection.prepareStatement("DELETE FROM Ban WHERE PlayerID = ?")) {
            statement.setString(1, player);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


