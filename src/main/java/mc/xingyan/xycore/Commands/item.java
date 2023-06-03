package mc.xingyan.xycore.Commands;

import mc.xingyan.xycore.Items.UPGRADEABLE_SWORD;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

import static mc.xingyan.xycore.Stasis.VowelConsonant.VowelConsonant;
import static mc.xingyan.xycore.getrank.getrank;

public class item implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println(label);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(getrank(player).equals("ADMIN")){
                if (args.length == 0) {
                    player.sendMessage("Usage: /" + label);
                    return true;
                }
                if (args.length >= 1) {
                    if (args[0].equals("UPGRADEABLE_SWORD")) {
                        if (args.length >= 2) {
                            player.getInventory().addItem(new UPGRADEABLE_SWORD().get(args[1].toUpperCase()));
                        } else {
                            player.getInventory().addItem(new UPGRADEABLE_SWORD().get("WOODEN"));
                        }
                        player.sendMessage(ChatColor.GREEN + "Gave "+player.getName()+" "+VowelConsonant("UPGRADEABLE_SWORD")+" UPGRADEABLE_SWORD");
                        return true;
                    }
                    if(args.length>=2){
                        try{
                            Integer.parseInt(args[1]);
                        }catch (NumberFormatException e){
                            player.sendMessage(ChatColor.RED+"Unknown Number.");
                            return true;
                        }
                        if(Material.getMaterial(args[0].toUpperCase())!=null){
                            ItemStack item = new ItemStack(Material.getMaterial(args[0].toUpperCase()));
                            item.setAmount(Integer.parseInt(args[1]));
                            player.getInventory().addItem(item);
                            player.sendMessage(ChatColor.GREEN + "Gave "+player.getName()+" "+VowelConsonant(args[0].toUpperCase())+" "+args[0].toUpperCase()+"x"+args[1]);
                        }else{
                            player.sendMessage(ChatColor.RED+"Not Found Item "+args[0].toUpperCase());
                        }
                        return true;
                    }else{
                        if(Material.getMaterial(args[0].toUpperCase())!=null){
                            player.getInventory().addItem(new ItemStack(Material.getMaterial(args[0].toUpperCase())));
                            player.sendMessage(ChatColor.GREEN + "Gave "+player.getName()+" "+VowelConsonant(args[0].toUpperCase())+" "+args[0].toUpperCase());
                        }else{
                            player.sendMessage(ChatColor.RED+"Not Found Item "+args[0].toUpperCase());
                        }
                        return true;
                    }
                } else {
                    sender.sendMessage("Available item: Sword");
                    return true;
                }
            }else{
                sender.sendMessage(ChatColor.RED+"You don't have permission to run that command!");
            }
        }
        return true;
    }
}
