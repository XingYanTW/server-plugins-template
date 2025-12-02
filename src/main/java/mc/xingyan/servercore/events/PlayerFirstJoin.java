package mc.xingyan.servercore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mc.xingyan.servercore.ServerCore;

public class PlayerFirstJoin implements Listener {

    @EventHandler
    public void onfirstjoin(PlayerJoinEvent event){
        try (PreparedStatement statement = ServerCore.connection.prepareStatement("SELECT * FROM Ranks WHERE PlayerUUID = ?")) {
            statement.setString(1, event.getPlayer().getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    try (PreparedStatement insertStmt = ServerCore.connection.prepareStatement("INSERT INTO Ranks (PlayerUUID, PlayerRank) VALUES (?, ?)")) {
                        insertStmt.setString(1, event.getPlayer().getUniqueId().toString());
                        insertStmt.setString(2, "DEFAULT");
                        insertStmt.executeUpdate();
                        System.out.println("Success! Inserted rank for: " + event.getPlayer().getName());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

