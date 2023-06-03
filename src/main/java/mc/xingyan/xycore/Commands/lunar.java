package mc.xingyan.xycore.Commands;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketStaffModState;
import com.lunarclient.bukkitapi.object.StaffModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.xycore.getrank.getrank;

public class lunar implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getrank(player).equals("ADMIN")){
                if(!LunarClientAPI.getInstance().isRunningLunarClient(player)){
                    player.sendMessage(ChatColor.RED+"You are not running Lunar Client!");
                    return true;
                }
                if(args.length==0){
                    player.sendMessage("Modules: staff");
                    return true;
                }
                if (args[0].equals("staff")) {
                    LunarClientAPI.getInstance().giveAllStaffModules(player);
                    player.sendMessage(ChatColor.GREEN + "Gave all staff modules to " + player.getName());
                    return true;
                }
            }
        }
        return true;
    }
}
