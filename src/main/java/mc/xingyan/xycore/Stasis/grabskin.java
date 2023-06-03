package mc.xingyan.xycore.Stasis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class grabskin {

    public String getTexture(String uuid){

        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid+"?unsigned=false");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject property = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            return property.get("value").getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSignature(String uuid){
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid+"?unsigned=false");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject property = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            return property.get("signature").getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
