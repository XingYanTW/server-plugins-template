package mc.xingyan.xycore.Stasis;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Field;

public class changeNick {

    public changeNick(String name, Player player) {
        Field ff;
        try {
            EntityPlayer ep = ((CraftPlayer) player).getHandle();
            GameProfile playerProfile = ep.getProfile();
            ff = playerProfile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(playerProfile, name);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            PlayerConnection pcp = ((CraftPlayer)p).getHandle().playerConnection;
            pcp.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle()));
            pcp.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle()));
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
