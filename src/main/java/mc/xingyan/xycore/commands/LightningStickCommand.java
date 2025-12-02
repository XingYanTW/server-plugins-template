package mc.xingyan.xycore.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import mc.xingyan.xycore.stasis.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class LightningStickCommand extends XyCommand implements Listener {

    @Override
    public String getName() {
        return "lightning_stick";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(PlayerInfo.getRank(player).equals("ADMIN")){
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
        if(is == null) return;
        if(!is.hasItemMeta()) return;
        if(!is.getItemMeta().hasDisplayName()) return;

        Player player = event.getPlayer();
        Location loc = player.getTargetBlock((Set<Material>) null, 100).getLocation();
        if(is.getType().equals(Material.STICK) && is.getItemMeta().getDisplayName().equals(ChatColor.YELLOW+"Lightning Stick")){
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            
            Bukkit.getOnlinePlayers().forEach(players->{
                PacketContainer lightning = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
                lightning.getIntegers().write(0, ThreadLocalRandom.current().nextInt());
                lightning.getUUIDs().write(0, UUID.randomUUID());
                lightning.getEntityTypeModifier().write(0, EntityType.LIGHTNING_BOLT);
                lightning.getDoubles().write(0, loc.getX());
                lightning.getDoubles().write(1, loc.getY());
                lightning.getDoubles().write(2, loc.getZ());
                
                try {
                    protocolManager.sendServerPacket(players, lightning);
                    players.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100f, 1f);
                } catch (Exception e) { e.printStackTrace(); }
            });
        }
    }
}
