package mc.xingyan.servercore.stasis;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mc.xingyan.servercore.ServerCore;

public class PlayerInfo {

    public static String getRank(Player player) {
        try (PreparedStatement statement = ServerCore.connection.prepareStatement("SELECT PlayerRank FROM Ranks WHERE PlayerUUID = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("PlayerRank");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "DEFAULT";
    }

    public static String getPrefix(Player player){
        String rank = getRank(player);
        switch (rank){
            case "DEFAULT":
                return ChatColor.GRAY+"";
            case "ADMIN":
                return ChatColor.RED+"[ADMIN] ";
            case "YOUTUBER":
                return ChatColor.RED+"["+ChatColor.RESET+"Youtuber"+ChatColor.RED+"] ";
            case "MODERATOR":
                return ChatColor.DARK_GREEN+"[MOD] ";
        }
        return rank;
    }

    public static String getRankColor(Player player){
        String rank = getRank(player);
        switch (rank){
            case "DEFAULT":
                return ChatColor.GRAY+"Default";
            case "ADMIN":
                return ChatColor.RED+"ADMIN";
            case "YOUTUBER":
                return ChatColor.RESET+"YOUTUBER";
            case "MODERATOR":
                return ChatColor.DARK_GREEN+"MODERATOR";
        }
        return rank;
    }

}

