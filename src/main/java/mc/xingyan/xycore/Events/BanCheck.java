package mc.xingyan.xycore.Events;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BanCheck implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(checkban(event.getPlayer())){
            event.getPlayer().kickPlayer(getBannedReason(event.getPlayer()));
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent event){
        if(checkban(event.getPlayer())){
            event.getPlayer().kickPlayer(getBannedReason(event.getPlayer()));
            event.setQuitMessage("");
        }
    }

    private static boolean checkban(Player player){
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("xycore");
            MongoCollection<Document> collection = database.getCollection("Ban");
            Document founded = collection.find(new Document("PlayerUUID", player.getUniqueId().toString())).first();
            if (founded != null) {
                return true;
            }
            return false;
        }
    }

    private static String getBannedReason(Player player){
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("xycore");
            MongoCollection<Document> collection = database.getCollection("Ban");
            Document founded = collection.find(new Document("PlayerUUID", player.getUniqueId().toString())).first();
            if (founded != null) {
                return founded.get("BannedReason").toString();
            }
            return null;
        }
    }

}
