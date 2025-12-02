package mc.xingyan.servercore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mc.xingyan.servercore.ServerCore;

public class BanCheck implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(checkban(event.getPlayer())){
            event.getPlayer().kickPlayer(getBannedReason(event.getPlayer()));
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent event){
        if(checkban(event.getPlayer())){
            event.getPlayer().kickPlayer(getBannedReason(event.getPlayer()));
            event.setQuitMessage("");
        }
    }

    private static boolean checkban(Player player){
        try (PreparedStatement statement = ServerCore.connection.prepareStatement("SELECT * FROM Ban WHERE PlayerUUID = ?")) {
            statement.setString(1, player.getUniqueId().toString());
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

    private static String getBannedReason(Player player){
        try (PreparedStatement statement = ServerCore.connection.prepareStatement("SELECT BannedReason FROM Ban WHERE PlayerUUID = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("BannedReason");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

