package mc.xingyan.servercore;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RankManager {


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

}


