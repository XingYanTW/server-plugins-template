package mc.xingyan.servercore.commands;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mc.xingyan.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.*;
import java.net.URL;

import static mc.xingyan.servercore.RankManager.getRank;

public class SkinCommand extends CoreCommand {

    @Override
    public String getName() {
        return "skin";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        String rank = getRank(player);
        if (!rank.equals("YOUTUBER") && !rank.equals("MODERATOR") && !rank.equals("ADMIN")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You Need YOUTUBER rank or higher to do this."));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /skin <player_name>|remove"));
            return true;
        }

        if (args[0].equals("remove")) {
            try {
                JsonObject json = getJson("https://sessionserver.mojang.com/session/minecraft/profile/" + player.getUniqueId() + "?unsigned=false");
                JsonObject property = json.get("properties").getAsJsonArray().get(0).getAsJsonObject();
                String texture = property.get("value").getAsString();
                String signature = property.get("signature").getAsString();
                changeSkin(player, texture, signature);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Skin reset to original."));
            } catch (Exception e) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Failed to reset skin: " + e.getMessage()));
                e.printStackTrace();
            }
        } else {
            try {
                JsonObject profileJson = getJson("https://api.mojang.com/users/profiles/minecraft/" + args[0]);
                if (profileJson == null || !profileJson.has("id")) {
                     player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Player not found."));
                     return true;
                }
                String id = profileJson.get("id").getAsString();
                String name = profileJson.get("name").getAsString();

                JsonObject sessionJson = getJson("https://sessionserver.mojang.com/session/minecraft/profile/" + id + "?unsigned=false");
                JsonObject property = sessionJson.get("properties").getAsJsonArray().get(0).getAsJsonObject();
                String texture = property.get("value").getAsString();
                String signature = property.get("signature").getAsString();
                
                changeSkin(player, texture, signature);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You have changed your skin to " + name));
            } catch (Exception e) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Failed to change skin: " + e.getMessage()));
                e.printStackTrace();
            }
        }
        return true;
    }

    private void changeSkin(Player sender, String texture, String signature) {
        Player player = sender;
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new WrappedSignedProperty("textures", texture, signature));

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.hidePlayer(ServerCore.plugin, player);
            p.showPlayer(ServerCore.plugin, player);
        });

        Location loc = player.getLocation();
        int food = player.getFoodLevel();
        double heal = player.getHealth();
        
        org.bukkit.inventory.ItemStack[] contents = player.getInventory().getContents();
        org.bukkit.inventory.ItemStack[] armor = player.getInventory().getArmorContents();
        
        player.getInventory().clear();
        player.setHealth(0D);
        player.spigot().respawn();
        player.teleport(loc);
        
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armor);
        player.setHealth(heal);
        player.setFoodLevel(food);
    }

    private JsonObject getJson(String urlString) throws IOException {
        URL url = new URL(urlString);
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }
}


