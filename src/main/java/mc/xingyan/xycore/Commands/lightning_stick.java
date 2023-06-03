package mc.xingyan.xycore.Commands;

import mc.xingyan.xycore.Stasis.playerinfo;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

public class lightning_stick implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(playerinfo.getrank(player).equals("ADMIN")){
                ItemStack is = new ItemStack(Material.STICK);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(ChatColor.YELLOW+"Lightning Stick");
                is.setItemMeta(im);
                player.getInventory().addItem(is);
            }
        }

        return true;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event){
        ItemStack is = event.getItem();
        Player player = event.getPlayer();
        Location loc = player.getTargetBlock((Set<Material>) null, 100).getLocation();
        if(is.getType().equals(Material.STICK) && is.getItemMeta().getDisplayName().equals(ChatColor.YELLOW+"Lightning Stick")){
            WorldServer ws = ((CraftWorld) player.getWorld()).getHandle();
            EntityLightning lightning = new EntityLightning(ws, loc.getX(), loc.getY(), loc.getZ());
            Bukkit.getOnlinePlayers().forEach(players->{
                PlayerConnection cp = ((CraftPlayer) players).getHandle().playerConnection;
                cp.sendPacket(new PacketPlayOutSpawnEntityWeather(lightning));
                cp.sendPacket(new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", loc.getX(), loc.getY(), loc.getZ(), 100.0F, 1));
            });
        }
    }
}
