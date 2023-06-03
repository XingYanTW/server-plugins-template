package mc.xingyan.xycore;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mc.xingyan.xycore.Commands.*;
import mc.xingyan.xycore.Events.*;
import mc.xingyan.xycore.Items.UPGRADEABLE_SWORD;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class main extends JavaPlugin {

    public static main plugin = null;

    public static MongoDatabase database;



    private static final List<Player> vanish = new ArrayList<>();
    private static final List<Player> nicked = new ArrayList<>();

    public static List<Player> getVanish() {
        return vanish;
    }

    public static Map<Player, String> realname = new HashMap<>();

    public static List<Player> getNicked() {
        return nicked;
    }

    public static Map<String, EntityArmorStand> armorStandMap = new HashMap<>();


    @Override
    public void onEnable() {



        Bukkit.getOnlinePlayers().forEach(player -> {
            player.kickPlayer("Please Rejoin The Server.");
        });

        plugin=this;
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            database = mongoClient.getDatabase("xycore");
            MongoCollection<Document> collection = database.getCollection("Ranks");
            collection.find().first();
        }
        this.getServer().getPluginManager().registerEvents(new PlayerFirstJoin(), this);
        this.getServer().getPluginManager().registerEvents(new OverrideCommand(), this);
        this.getServer().getPluginManager().registerEvents(new BanCheck(), this);
        this.getServer().getPluginManager().registerEvents(new UPGRADEABLE_SWORD(), this);
        this.getServer().getPluginManager().registerEvents(new DeathMessage(), this);
        this.getServer().getPluginManager().registerEvents(new VanishCheck(), this);
        this.getServer().getPluginManager().registerEvents(new flycheck(), this);
        this.getServer().getPluginManager().registerEvents(new Weather(), this);
        this.getServer().getPluginManager().registerEvents(new JoinQuitMessage(), this);
        this.getServer().getPluginManager().registerEvents(new TabName(), this);
        this.getServer().getPluginManager().registerEvents(new Chat(), this);
        this.getServer().getPluginManager().registerEvents(new ScoreBoard(), this);
        this.getServer().getPluginManager().registerEvents(new lightning_stick(), this);


        this.getCommand("setrank").setExecutor(new setRank());
        this.getCommand("opme").setExecutor(new opme());
        this.getCommand("kick").setExecutor(new kick());
        this.getCommand("ban").setExecutor(new ban());
        this.getCommand("unban").setExecutor(new unban());
        this.getCommand("item").setExecutor(new item());
        this.getCommand("skin").setExecutor(new skin());
        this.getCommand("nick").setExecutor(new nick());
        this.getCommand("unnick").setExecutor(new unnick());
        this.getCommand("vanish").setExecutor(new vanish());
        this.getCommand("fly").setExecutor(new fly());
        this.getCommand("lunar").setExecutor(new lunar());
        this.getCommand("test").setExecutor(new test());
        this.getCommand("lightning_stick").setExecutor(new lightning_stick());

        this.getCommand("item").setTabCompleter(new mc.xingyan.xycore.TabComplete.item());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                getVanish().forEach(player -> {
                    if(getNicked().contains(player)){
                        String message = "You Are Current "+ChatColor.RED+"Vanished"+ChatColor.RESET+", "+ChatColor.RED+"Nicked";
                        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
                        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
                    }else{
                        String message = "You Are Current "+ChatColor.RED+"Vanished";
                        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
                        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
                    }
                });
                getNicked().forEach(player -> {
                    if(!getVanish().contains(player)){
                        String message = "You Are Current "+ChatColor.RED+"Nicked";
                        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
                        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
                    }
                });
            }
        },0, 20);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static main getPlugin(){
        return plugin;
    }

}
