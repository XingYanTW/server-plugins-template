package mc.xingyan.xycore.Commands;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static mc.xingyan.xycore.getrank.getrank;

public class unban implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            if(args.length>=1){
                if(checkban(args[0])){
                    removeban(args[0]);
                    System.out.println(ChatColor.GREEN+"Successful Unbanned Player!");
                }else{
                    System.out.println(ChatColor.RED+"Player Not Found.");
                }
            }
        }
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (getrank(player).equals("MODERATOR") || getrank(player).equals("ADMIN")) {
                if(args.length>=1){
                    if(checkban(args[0])){
                        removeban(args[0]);
                        player.sendMessage(ChatColor.GREEN+"Successful Unbanned Player!");
                    }else{
                        player.sendMessage(ChatColor.RED+"Player Not Found.");
                    }
                }
            }
        }

        return true;
    }

    private static boolean checkban(String player){
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("xycore");
            MongoCollection<Document> collection = database.getCollection("Ban");
            Document founded = collection.find(new Document("PlayerID", player)).first();
            if (founded != null) {
                return true;
            }
            return false;
        }
    }

    private static void removeban(String player){
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("xycore");
            MongoCollection<Document> collection = database.getCollection("Ban");
            Document founded = collection.find(new Document("PlayerID", player)).first();
            if (founded != null) {
                collection.deleteOne(founded);
            }
        }
    }
}
