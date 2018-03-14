package me.mawood.loraCapture.spark;

import static me.mawood.loraCapture.spark.JsonUtil.json;
import static spark.Spark.post;

import com.google.gson.Gson;
import me.mawood.loraCapture.persistence.PersistenceManager;
import me.mawood.loraCapture.persistence.loRaPacket.LoRaPacket;

import java.util.ArrayList;
import java.util.Arrays;


public class CaptureEndpoint
{
    private final ArrayList<PacketListener> listeners;
    private final PersistenceManager pm;

    public CaptureEndpoint(PersistenceManager pm)
    {
        this.pm = pm;
        listeners = new ArrayList<>();
        post("/", (request, response) -> {
            Gson gson = new Gson();
            LoRaPacket p = gson.fromJson(request.body(), LoRaPacket.class);

            pm.store(p);
            System.out.println(p);
            alertListeners(p);
            response.type("application/json");
            return "";
        }, json());
    }

    public void registerInterest(PacketListener... newListeners)
    {
        listeners.addAll(Arrays.asList(newListeners));
    }

    private void alertListeners(LoRaPacket packet)
    {
        for(PacketListener listener:listeners) listener.handlePacket(packet);
    }
}
