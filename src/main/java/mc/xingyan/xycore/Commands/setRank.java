package mc.xingyan.xycore.Commands;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class setRank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender){
            if(args.length>=2){
                if(Bukkit.getPlayer(args[0]) !=null){
                    Player player = Bukkit.getPlayer(args[0]);
                    if(checkrank(args[1])){
                        String uri = "mongodb://localhost:27017";
                        try (MongoClient mongoClient = MongoClients.create(uri)) {
                            MongoDatabase database = mongoClient.getDatabase("xycore");
                            MongoCollection<Document> collection = database.getCollection("Ranks");
                            Document found = collection.find(new Document( "PlayerUUID", player.getUniqueId().toString())).first();
                            if(found !=null){
                                System.out.println(found.toJson());
                                Bson updatedvalue = new Document("PlayerRank", args[1].toUpperCase(Locale.ROOT));
                                Bson updateoperation = new Document("$set", updatedvalue);
                                collection.updateOne(found, updateoperation);
                                player.sendMessage(ChatColor.GREEN+"You Are Now "+args[1].toUpperCase(Locale.ROOT));
                                sender.sendMessage(ChatColor.GREEN+"Successful Set "+player.getName()+"'s Rank to "+ args[1].toUpperCase(Locale.ROOT));
                            }
                        }
                    }else{
                        sender.sendMessage(ChatColor.RED+"Rank Not Found!");
                    }
                }
            }

        }
        if(sender instanceof Player){
            Player p = (Player) sender;
            String uri = "mongodb://localhost:27017";
            try (MongoClient mongoClient = MongoClients.create(uri)) {
                MongoDatabase database = mongoClient.getDatabase("xycore");
                MongoCollection<Document> collection = database.getCollection("Ranks");
                Document founded = collection.find(new Document( "PlayerUUID", p.getUniqueId().toString())).first();
                if(founded !=null){
                    if(founded.get("PlayerRank").equals("ADMIN")){
                        if(args.length>=2){
                            if(Bukkit.getPlayer(args[0]) !=null){
                                Player player = Bukkit.getPlayer(args[0]);
                                if(checkrank(args[1])){
                                    Document found = collection.find(new Document( "PlayerUUID", player.getUniqueId().toString())).first();
                                    if(found !=null){
                                        System.out.println(found.toJson());
                                        Bson updatedvalue = new Document("PlayerRank", args[1].toUpperCase(Locale.ROOT));
                                        Bson updateoperation = new Document("$set", updatedvalue);
                                        collection.updateOne(found, updateoperation);
                                        player.sendMessage(ChatColor.GREEN+"You Are Now "+args[1].toUpperCase(Locale.ROOT));
                                        p.sendMessage(ChatColor.GREEN+"Successful Set "+player.getName()+"'s Rank to "+ args[1].toUpperCase(Locale.ROOT));
                                    }
                                }else{
                                    sender.sendMessage(ChatColor.RED+"Rank Not Found!");
                                }
                            }
                        }
                    }else{
                        sender.sendMessage(ChatColor.RED+"You don't have permission to run that command!");
                    }
                }
            }
        }
        return true;
    }


    private static boolean checkrank(String rank){
        switch (rank.toUpperCase(Locale.ROOT)){
            case "ADMIN":
            case "MODERATOR":
            case "YOUTUBER":
            case "DEFAULT":
                return true;
            default:
                return false;
        }
    }
}
