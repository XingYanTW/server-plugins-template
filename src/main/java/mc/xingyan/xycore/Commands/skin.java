package mc.xingyan.xycore.Commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

import static mc.xingyan.xycore.getrank.getrank;

public class skin implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(getrank(player).equals("YOUTUBER") || getrank(player).equals("MODERATOR") || getrank(player).equals("ADMIN")){
                if (args.length >= 1) {
                    if (args[0].equals("remove")) {

                        String texture;
                        String signature;

                        try {
                            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+player.getUniqueId()+"?unsigned=false");
                            InputStreamReader reader = new InputStreamReader(url.openStream());
                            JsonObject property = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                            texture = property.get("value").getAsString();
                            signature = property.get("signature").getAsString();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        chageskin((Player) sender, texture, signature);

                    }else{
                        try {
                            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+args[0]);
                            InputStreamReader reader = new InputStreamReader(url.openStream());
                            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
                            String id = json.get("id").getAsString();
                            String name = json.get("name").getAsString();
                            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+id+"?unsigned=false");
                            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
                            JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                            String texture = property.get("value").getAsString();
                            String signature = property.get("signature").getAsString();
                            chageskin((Player) sender, texture, signature);
                            player.sendMessage(ChatColor.GREEN+"You has changed your skin to "+name);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }

                }
            }else{
                sender.sendMessage(ChatColor.RED+"You Need YOUTUBER rank or higher to do this.");
            }
            return true;

        }
        return true;
    }

    private void chageskin(Player sender, String texture, String signature) {
        Player player = sender;
        PlayerConnection cp = ((CraftPlayer) player).getHandle().playerConnection;
        GameProfile gp = ((CraftPlayer) player).getHandle().getProfile();

        cp.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle()));
        gp.getProperties().removeAll("textures");
        gp.getProperties().put("textures", new Property("textures", texture, signature));

        cp.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle()));
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
        Bukkit.getOnlinePlayers().forEach(p -> {
            PlayerConnection pcp = ((CraftPlayer)p).getHandle().playerConnection;
            pcp.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle()));
            pcp.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle()));
        });
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject();
            return json;
        } finally {
            is.close();
        }
    }

    public static void main(String[] args) throws IOException {
        JSONObject json = readJsonFromUrl("https://graph.facebook.com/19292868552");
        System.out.println(json.toString());
        System.out.println(json.get("id"));
    }
}
