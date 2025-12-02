package mc.xingyan.xycore.stasis;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerInfo {

    public static String getRank(Player player) {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("xycore");
            MongoCollection<Document> collection = database.getCollection("Ranks");
            Document founded = collection.find(new Document("PlayerUUID", player.getUniqueId().toString())).first();
            if (founded != null) {
                return founded.get("PlayerRank").toString();
            }
            return "DEFAULT";
        }
    }

    public static String getPrefix(Player player){
        String rank = getRank(player);
        switch (rank){
            case "DEFAULT":
                return ChatColor.GRAY+"";
            case "ADMIN":
                return ChatColor.RED+"[ADMIN] ";
            case "YOUTUBER":
                return ChatColor.RED+"["+ChatColor.RESET+"Youtuber"+ChatColor.RED+"] ";
            case "MODERATOR":
                return ChatColor.DARK_GREEN+"[MOD] ";
        }
        return rank;
    }

    public static String getRankColor(Player player){
        String rank = getRank(player);
        switch (rank){
            case "DEFAULT":
                return ChatColor.GRAY+"Default";
            case "ADMIN":
                return ChatColor.RED+"ADMIN";
            case "YOUTUBER":
                return ChatColor.RESET+"YOUTUBER";
            case "MODERATOR":
                return ChatColor.DARK_GREEN+"MODERATOR";
        }
        return rank;
    }

}
