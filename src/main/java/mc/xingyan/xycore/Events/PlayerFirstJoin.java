package mc.xingyan.xycore.Events;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.print.Doc;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;


public class PlayerFirstJoin implements Listener {

    @EventHandler
    public void onfirstjoin(PlayerJoinEvent event){
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("xycore");
            MongoCollection<Document> collection = database.getCollection("Ranks");
            Bson projectionFields = Projections.fields(
                    Projections.include("PlayerUUID", "imdb"),
                    Projections.excludeId());
            Document doc = collection.find(eq("PlayerUUID", event.getPlayer().getUniqueId().toString()))
                    .projection(projectionFields)
                    .sort(Sorts.descending("imdb.rating"))
                    .first();
            if (doc == null) {
                try {
                    InsertOneResult result = collection.insertOne(new Document()
                            .append("PlayerUUID", event.getPlayer().getUniqueId().toString())
                            .append("PlayerID", event.getPlayer().getName())
                            .append("PlayerRank", "DEFAULT"));
                    System.out.println("Success! Inserted document id: " + result.getInsertedId());
                } catch (MongoException me) {
                    System.err.println("Unable to insert due to an error: " + me);
                }
            }
        }

    }
}
