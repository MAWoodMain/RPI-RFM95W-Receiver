package me.mawood.rfm95w;

import static me.mawood.JsonUtil.json;
import static spark.Spark.post;

import com.google.gson.Gson;
import me.mawood.persistence.PersistenceManager;
import me.mawood.persistence.loRaPacket.LoRaPacket;


public class SparkMain
{

    public static void main(String[] args)
    {
        new SparkMain();
    }

    public SparkMain()
    {
        post("/", (request, response) -> {
            Gson gson = new Gson();
            LoRaPacket p = gson.fromJson(request.body(), LoRaPacket.class);

            PersistenceManager pm = new PersistenceManager();
            pm.store(p);
            System.out.println(p);
            response.type("application/json");
            return "";
        }, json());
    }
}
