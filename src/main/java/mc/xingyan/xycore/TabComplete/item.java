package mc.xingyan.xycore.TabComplete;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class item implements TabCompleter {

    List<String> COMMANDS = new ArrayList<>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        for(Material mat : Material.values()){
            COMMANDS.add(mat.name());
        }
        //create new array
        final List<String> completions = new ArrayList<>();
        //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(args[0], COMMANDS, completions);
        //sort the list
        Collections.sort(completions);
        //Edit: sorting the list it's not required anymore
        return completions;
    }

}
