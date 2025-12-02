package mc.xingyan.xycore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import mc.xingyan.xycore.commands.*;
import mc.xingyan.xycore.events.*;
import mc.xingyan.xycore.items.UpgradeableSword;
import mc.xingyan.xycore.tabcomplete.ItemTabCompleter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class XyCore extends JavaPlugin {

    public static XyCore plugin = null;

    public static MongoDatabase database;
    private ProtocolManager protocolManager;



    private static final List<Player> vanish = new ArrayList<>();
    private static final List<Player> nicked = new ArrayList<>();

    public static List<Player> getVanish() {
        return vanish;
    }

    public static Map<Player, String> realname = new HashMap<>();

    public static List<Player> getNicked() {
        return nicked;
    }

    public static Map<String, Integer> armorStandMap = new HashMap<>();


    @Override
    public void onEnable() {
        protocolManager = ProtocolLibrary.getProtocolManager();


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

        registerEvents(
                new PlayerFirstJoin(),
                new OverrideCommand(),
                new BanCheck(),
                new UpgradeableSword(),
                new DeathMessage(),
                new VanishCheck(),
                new FlyCheck(),
                new Weather(),
                new JoinQuitMessage(),
                new TabName(),
                new Chat(),
                new ScoreBoard(),
                new LightningStickCommand()
        );

        registerCommand(new SetRankCommand());
        registerCommand(new OpMeCommand());
        registerCommand(new KickCommand());
        registerCommand(new BanCommand());
        registerCommand(new UnbanCommand());
        registerCommand(new ItemCommand());
        registerCommand(new SkinCommand());
        registerCommand(new NickCommand());
        registerCommand(new UnnickCommand());
        registerCommand(new VanishCommand());
        registerCommand(new FlyCommand());
        registerCommand(new TestCommand());
        registerCommand(new LightningStickCommand());

        this.getCommand("item").setTabCompleter(new ItemTabCompleter());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                getVanish().forEach(player -> {
                    if(getNicked().contains(player)){
                        String message = "You Are Current "+ChatColor.RED+"Vanished"+ChatColor.RESET+", "+ChatColor.RED+"Nicked";
                        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SYSTEM_CHAT);
                        packet.getStrings().write(0, "{\"text\":\"" + message + "\"}");
                        packet.getBooleans().write(0, true);
                        try {
                            protocolManager.sendServerPacket(player, packet);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }else{
                        String message = "You Are Current "+ChatColor.RED+"Vanished";
                        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SYSTEM_CHAT);
                        packet.getStrings().write(0, "{\"text\":\"" + message + "\"}");
                        packet.getBooleans().write(0, true);
                        try {
                            protocolManager.sendServerPacket(player, packet);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
                getNicked().forEach(player -> {
                    if(!getVanish().contains(player)){
                        String message = "You Are Current "+ChatColor.RED+"Nicked";
                        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SYSTEM_CHAT);
                        packet.getStrings().write(0, "{\"text\":\"" + message + "\"}");
                        packet.getBooleans().write(0, true);
                        try {
                            protocolManager.sendServerPacket(player, packet);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        },0, 20);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommand(XyCommand command) {
        if (getCommand(command.getName()) != null) {
            getCommand(command.getName()).setExecutor(command);
        } else {
            getLogger().warning("Command " + command.getName() + " not found in plugin.yml");
        }
    }


    public static XyCore getPlugin(){
        return plugin;
    }

}
