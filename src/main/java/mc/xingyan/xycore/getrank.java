package mc.xingyan.xycore;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.entity.Player;

public class getrank {


    public static String getrank(Player player) {
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

}
