package me.mawood.loraCapture.spark;

import static me.mawood.loraCapture.spark.JsonUtil.json;
import static spark.Spark.port;
import static spark.Spark.post;

import com.google.gson.Gson;
import me.mawood.loraCapture.persistence.PersistenceManager;
import me.mawood.loraCapture.persistence.loRaPacket.LoRaPacket;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;


public class CaptureEndpoint
{
    private final ArrayList<PacketListener> listeners;
    private final PersistenceManager pm;

    public CaptureEndpoint(PersistenceManager pm)
    {
        this.pm = pm;
        port(20054);
        listeners = new ArrayList<>();
        post("/", (request, response) -> {
            Gson gson = new Gson();
            System.out.println(request.body());
            LoRaPacket p = gson.fromJson(request.body(), LoRaPacket.class);
            if(p.getRxInfo().length > 0)
            {
                if(p.getRxInfo()[0].getTime() == null)
                {
                    p.getRxInfo()[0].setTime(Instant.now().toString());
                } else if (p.getRxInfo()[0].getTime().length() < 5)
                {
                    p.getRxInfo()[0].setTime(Instant.now().toString());
                }

            }
            System.out.println(p);
            pm.store(p);
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
