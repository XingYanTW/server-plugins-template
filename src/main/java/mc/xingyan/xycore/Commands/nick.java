package mc.xingyan.xycore.Commands;

import mc.xingyan.xycore.Stasis.changeNick;
import mc.xingyan.xycore.main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.xycore.getrank.getrank;


public class nick implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getrank(player).equals("YOUTUBER") || getrank(player).equals("MODERATOR") || getrank(player).equals("ADMIN")){
                if(main.getNicked().contains(player)){
                    player.sendMessage(ChatColor.RED+"You Are Already Nicked");
                    return true;
                }
                if(args.length>=1){
                    main.realname.put(player, player.getName());
                    System.out.println(main.realname.get(player));
                    if(args[0].length() <=16){
                        player.setDisplayName(args[0]);
                        player.setPlayerListName(args[0]);

                        new changeNick(args[0], player);

                        main.getNicked().add(player);

                        player.sendMessage(ChatColor.GREEN+"You Has Changed Your Nickname To "+args[0]);
                    }else{
                        player.sendMessage(ChatColor.RED+"Nickname can't longer than 16 charters.");
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED+"You Need YOUTUBER rank or higher to do this.");
            }
        }

        return true;
    }
}
