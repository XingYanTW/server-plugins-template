package mc.xingyan.xycore.Commands;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static mc.xingyan.xycore.getrank.getrank;

public class ban implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(getrank(player).equals("MODERATOR") || getrank(player).equals("ADMIN")){
                if(args.length>=1){
                    if(Bukkit.getPlayer(args[0]) !=null ){
                        Player target = Bukkit.getPlayer(args[0]);
                        if(args.length>=2){
                            String reason = "";
                            for(int i=1; i<=args.length-1; i++){
                                reason += args[i] + " ";
                            }
                            target.kickPlayer(ChatColor.translateAlternateColorCodes('&', ChatColor.RED+"You has been banned from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+reason+"\n\n"+ChatColor.GRAY+sdf.format(timestamp)));
                            banplayer(target, ChatColor.translateAlternateColorCodes('&', ChatColor.RED+"You has been banned from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+reason+"\n\n"+ChatColor.GRAY+sdf.format(timestamp)));
                            Bukkit.getServer().broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"[Notify] "+target.getName()+" Has Been Banned by Moderator. Reason: "+ChatColor.RESET+reason);
                        }else{
                            target.kickPlayer(ChatColor.RED+"You has been banned from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+"Banned\n\n"+ChatColor.GRAY+sdf.format(timestamp));
                            banplayer(target, ChatColor.RED+"You has been banned from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+"Banned\n\n"+ChatColor.GRAY+sdf.format(timestamp));
                            Bukkit.getServer().broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"[Notify] "+target.getName()+" Has Been Banned by Moderator. Reason: "+ChatColor.RESET+"Banned");
                        }
                    }
                }else{
                    player.sendMessage(ChatColor.RED+"Usage: /ban <player> [<reason>]");
                }
            }else {
                sender.sendMessage(ChatColor.RED+"You don't have permission to run that command!");
            }
        }
        if(sender instanceof ConsoleCommandSender){
            if(args.length>=1){
                if(Bukkit.getPlayer(args[0]) !=null ){
                    Player target = Bukkit.getPlayer(args[0]);
                    if(args.length>=2){
                        String reason = "";
                        for(int i=1; i<=args.length-1; i++){
                            reason += args[i] + " ";
                        }
                        target.kickPlayer(ChatColor.translateAlternateColorCodes('&', ChatColor.RED+"You has been banned from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+reason+"\n\n"+ChatColor.GRAY+sdf.format(timestamp)));
                        banplayer(target, ChatColor.translateAlternateColorCodes('&', ChatColor.RED+"You has been banned from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+reason+"\n\n"+ChatColor.GRAY+sdf.format(timestamp)));
                        Bukkit.getServer().broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"[Notify] "+target.getName()+" Has Been Banned by Moderator. Reason: "+ChatColor.RESET+reason);
                    }else{
                        target.kickPlayer(ChatColor.RED+"You has been banned from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+"Banned\n\n"+ChatColor.GRAY+sdf.format(timestamp));
                        banplayer(target, ChatColor.RED+"You has been banned from this server!\n\n"+ChatColor.GRAY+"Reason: "+ChatColor.WHITE+"Banned\n\n"+ChatColor.GRAY+sdf.format(timestamp));
                        Bukkit.getServer().broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"[Notify] "+target.getName()+" Has Been Banned by Moderator. Reason: "+ChatColor.RESET+"Banned");
                    }
                }
            }else{
                sender.sendMessage(ChatColor.RED+"Usage: /ban <player> [<reason>]");
            }
        }
        return true;
    }

    private static String banplayer(Player player, String reason){
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("xycore");
            MongoCollection<Document> collection = database.getCollection("Ban");
            Document founded = collection.find(new Document("PlayerUUID", player.getUniqueId().toString())).first();
            if (founded != null) {
                return "ALREADY";
            }else{
                try {
                    InsertOneResult result = collection.insertOne(new Document()
                            .append("PlayerUUID", player.getUniqueId().toString())
                            .append("PlayerID", player.getName())
                            .append("BannedReason", reason));
                    System.out.println("Success! Inserted document id: " + result.getInsertedId());
                    return "SUCCESSFUL";
                } catch (MongoException me) {
                    System.err.println("Unable to insert due to an error: " + me);
                }
            }
        }
        return null;
    }


}
