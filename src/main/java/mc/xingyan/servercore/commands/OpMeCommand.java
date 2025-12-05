package mc.xingyan.servercore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static mc.xingyan.servercore.RankManager.getRank;

public class OpMeCommand extends CoreCommand {

    @Override
    public String getName() {
        return "opme";
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getRank(player).equals("ADMIN")){
                if(player.isOp()){
                    player.setOp(false);
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are no longer OP."));
                }else{
                    player.setOp(true);
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You are now OP."));
                }
            }else{
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are no longer OP."));
            }
        }

        return true;
    }
}


