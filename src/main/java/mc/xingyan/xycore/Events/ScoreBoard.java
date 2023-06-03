package mc.xingyan.xycore.Events;

import mc.xingyan.xycore.Stasis.playerinfo;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreBoard implements Listener {



    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Bukkit.getOnlinePlayers().forEach(players -> {
            updatescoreboard(players, Bukkit.getOnlinePlayers().size());
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Bukkit.getOnlinePlayers().forEach(player -> {
            updatescoreboard(player, Bukkit.getOnlinePlayers().size()-1);
        });
    }

    public void updatescoreboard(Player player, int PlayerList){
        String rank = playerinfo.getrankcolor(player);
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yy");

        PlayerConnection cp = ((CraftPlayer) player).getHandle().playerConnection;
        Scoreboard scoreboard = new Scoreboard();
        ScoreboardObjective obj = scoreboard.registerObjective(player.getName(), IScoreboardCriteria.b);
        obj.setDisplayName(ChatColor.GOLD+"Example Server");
        PacketPlayOutScoreboardObjective createPacket = new PacketPlayOutScoreboardObjective(obj, 0);
        PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, obj);


        ScoreboardScore a1 = new ScoreboardScore(scoreboard, obj, ChatColor.GRAY+ft.format(date));
        ScoreboardScore a2 = new ScoreboardScore(scoreboard, obj, "\u00A7" + Character.toString((char)('a' + 2)) + ChatColor.RESET);
        ScoreboardScore a3 = new ScoreboardScore(scoreboard, obj, "Rank: "+rank);
        ScoreboardScore a4 = new ScoreboardScore(scoreboard, obj, "\u00A7" + Character.toString((char)('a' + 3)) + ChatColor.RESET);
        ScoreboardScore a5 = new ScoreboardScore(scoreboard, obj, "Online: "+ChatColor.GREEN+PlayerList);
        ScoreboardScore a6 = new ScoreboardScore(scoreboard, obj, "\u00A7" + Character.toString((char)('a' + 4)) + ChatColor.RESET);
        ScoreboardScore a7 = new ScoreboardScore(scoreboard, obj, ChatColor.YELLOW+"play.example.com");
        a1.setScore(10);
        a2.setScore(9);
        a3.setScore(8);
        a4.setScore(7);
        a5.setScore(6);
        a6.setScore(5);
        a7.setScore(4);
        //a8.setScore(0);
        //a9.setScore(0);
        //a10.setScore(0);

        PacketPlayOutScoreboardObjective removePacket = new PacketPlayOutScoreboardObjective(obj, 1);
        PacketPlayOutScoreboardScore pa1 = new PacketPlayOutScoreboardScore(a1);
        PacketPlayOutScoreboardScore pa2 = new PacketPlayOutScoreboardScore(a2);
        PacketPlayOutScoreboardScore pa3 = new PacketPlayOutScoreboardScore(a3);
        PacketPlayOutScoreboardScore pa4 = new PacketPlayOutScoreboardScore(a4);
        PacketPlayOutScoreboardScore pa5 = new PacketPlayOutScoreboardScore(a5);
        PacketPlayOutScoreboardScore pa6 = new PacketPlayOutScoreboardScore(a6);
        PacketPlayOutScoreboardScore pa7 = new PacketPlayOutScoreboardScore(a7);
        //PacketPlayOutScoreboardScore pa8 = new PacketPlayOutScoreboardScore(a8);
        //PacketPlayOutScoreboardScore pa9 = new PacketPlayOutScoreboardScore(a9);
        //PacketPlayOutScoreboardScore pa10 = new PacketPlayOutScoreboardScore(a10);


        cp.sendPacket(removePacket);
        cp.sendPacket(createPacket);
        cp.sendPacket(display);

        cp.sendPacket(pa1);
        cp.sendPacket(pa2);
        cp.sendPacket(pa3);
        cp.sendPacket(pa4);
        cp.sendPacket(pa5);
        cp.sendPacket(pa6);
        cp.sendPacket(pa7);
        //cp.sendPacket(pa8);
        //cp.sendPacket(pa9);
        //cp.sendPacket(pa10);
    }

}
