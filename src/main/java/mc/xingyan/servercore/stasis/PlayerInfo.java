package mc.xingyan.servercore.stasis;

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
                return "<gray>";
            case "ADMIN":
                return "<red>[ADMIN] ";
            case "YOUTUBER":
                return "<red>[<reset>Youtuber<red>] ";
            case "MODERATOR":
                return "<dark_green>[MOD] ";
        }
        return rank;
    }

    public static String getRankColor(Player player){
        String rank = getRank(player);
        switch (rank){
            case "DEFAULT":
                return "<gray>Default";
            case "ADMIN":
                return "<red>ADMIN";
            case "YOUTUBER":
                return "<reset>YOUTUBER";
            case "MODERATOR":
                return "<dark_green>MODERATOR";
        }
        return rank;
    }

}


