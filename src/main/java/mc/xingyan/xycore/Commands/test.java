package mc.xingyan.xycore.Commands;

import mc.xingyan.xycore.main;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class test implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args[0].equals("spawn")) {

            Location loc = player.getLocation();

            WorldServer ws = ((CraftWorld) player.getWorld()).getHandle();
            EntityArmorStand stand = new EntityArmorStand(ws);
            stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            stand.setCustomName(player.getName());
            stand.setCustomNameVisible(true);
            stand.setInvisible(true);
            main.armorStandMap.put(player.getName(), stand);

            Bukkit.getOnlinePlayers().forEach(players -> {
                PlayerConnection cp = ((CraftPlayer) players).getHandle().playerConnection;
                cp.sendPacket(new PacketPlayOutSpawnEntityLiving(stand));
            });
        }
        if(args[0].equals("kill")){
            EntityArmorStand stand = main.armorStandMap.get(player.getName());
            Bukkit.getOnlinePlayers().forEach(players -> {
                PlayerConnection cp = ((CraftPlayer) players).getHandle().playerConnection;
                cp.sendPacket(new PacketPlayOutEntityDestroy(stand.getId()));
            });
        }


        return true;
    }
}
