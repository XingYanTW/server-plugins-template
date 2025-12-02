package mc.xingyan.xycore.stasis;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import mc.xingyan.xycore.XyCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Field;

public class ChangeNick {

    public ChangeNick(String nick, Player player) {
        try {
            WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
            Field nameField = profile.getHandle().getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(profile.getHandle(), nick);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.hidePlayer(XyCore.plugin, player);
            p.showPlayer(XyCore.plugin, player);
        });
        
        Location loc = player.getLocation();
        int food = player.getFoodLevel();
        double heal = player.getHealth();
        PlayerInventory inv = player.getInventory();
        player.getInventory().clear();
        player.setHealth(0D);
        player.spigot().respawn();
        player.teleport(loc);
        player.getInventory().setContents(inv.getContents());
        player.setHealth(heal);
        player.setFoodLevel(food);
    }

}
