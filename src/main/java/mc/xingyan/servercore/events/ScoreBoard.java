package mc.xingyan.servercore.events;

import mc.xingyan.servercore.stasis.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

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
        String rank = PlayerInfo.getRankColor(player);
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yy");

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective(player.getName(), "dummy");
        obj.setDisplayName(ChatColor.GOLD+"Example Server");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score s1 = obj.getScore(ChatColor.GRAY+ft.format(date));
        s1.setScore(10);

        Score s2 = obj.getScore("\u00A7" + Character.toString((char)('a' + 2)) + ChatColor.RESET);
        s2.setScore(9);

        Score s3 = obj.getScore("Rank: "+rank);
        s3.setScore(8);

        Score s4 = obj.getScore("\u00A7" + Character.toString((char)('a' + 3)) + ChatColor.RESET);
        s4.setScore(7);

        Score s5 = obj.getScore("Online: "+ChatColor.GREEN+PlayerList);
        s5.setScore(6);

        Score s6 = obj.getScore("\u00A7" + Character.toString((char)('a' + 4)) + ChatColor.RESET);
        s6.setScore(5);

        Score s7 = obj.getScore(ChatColor.YELLOW+"play.example.com");
        s7.setScore(4);

        player.setScoreboard(scoreboard);
    }

}

