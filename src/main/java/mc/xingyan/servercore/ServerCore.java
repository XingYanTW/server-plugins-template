package mc.xingyan.servercore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import mc.xingyan.servercore.commands.*;
import mc.xingyan.servercore.events.*;
import mc.xingyan.servercore.items.UpgradeableSword;
import mc.xingyan.servercore.tabcomplete.ItemTabCompleter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServerCore extends JavaPlugin {

    public static ServerCore plugin = null;

    public static Connection connection;
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
        
        try {
            File dataFolder = getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + new File(dataFolder, "database.db").getAbsolutePath());
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS Ranks (PlayerUUID TEXT PRIMARY KEY, PlayerRank TEXT)");
                statement.execute("CREATE TABLE IF NOT EXISTS Ban (PlayerUUID TEXT PRIMARY KEY, PlayerID TEXT, BannedReason TEXT)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        registerEvents(
                new PlayerFirstJoin(),
                new OverrideCommand(),
                new UpgradeableSword(),
                new DeathMessage(),
                new VanishCheck(),
                new FlyCheck(),
                new Weather(),
                new JoinQuitMessage(),
                new BanCheck(),
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
        registerCommand(new LightningStickCommand());

        this.getCommand("item").setTabCompleter(new ItemTabCompleter());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                getVanish().forEach(player -> {
                    Component message;
                    if(getNicked().contains(player)){
                        message = Component.text("You Are Current ")
                                .append(Component.text("Vanished").color((TextColor) Color.RED))
                                .append(Component.text(", "))
                                .append(Component.text("Nicked").color((TextColor) Color.RED));
                    }else{
                        message = Component.text("You Are Current ")
                                .append(Component.text("Vanished").color((TextColor) Color.RED));
                    }
                    player.sendActionBar(message);
                });
                getNicked().forEach(player -> {
                    if(!getVanish().contains(player)){
                        Component message = Component.text("You Are Current ")
                                .append(Component.text("Vanished").color((TextColor) Color.RED));
                        player.sendActionBar(message);
                    }
                });
            }
        },0, 20);

    }

    @Override
    public void onDisable() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommand(CoreCommand command) {
        if (getCommand(command.getName()) != null) {
            getCommand(command.getName()).setExecutor(command);
        } else {
            getLogger().warning("Command " + command.getName() + " not found in plugin.yml");
        }
    }


    public static ServerCore getPlugin(){
        return plugin;
    }

}


