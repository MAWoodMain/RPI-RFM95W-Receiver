package me.mawood.loraCapture;

import me.mawood.loraCapture.packet.DecodedPacket;
import me.mawood.loraCapture.packet.PacketException;
import me.mawood.loraCapture.packet.PacketStreamReader;
import me.mawood.loraCapture.packet.block.InvalidBlockException;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.packet.segment.Segment;
import me.mawood.loraCapture.packet.segment.segments.BatterySegment;
import me.mawood.loraCapture.persistence.PersistenceManager;
import me.mawood.loraCapture.spark.CaptureEndpoint;

import java.util.logging.Level;

public class Main
{
    public static void main(String[] args)
    {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.FINEST);

        PersistenceManager pm = new PersistenceManager();
        CaptureEndpoint endpoint = new CaptureEndpoint(pm);
        endpoint.registerInterest(System.out::println);
        endpoint.registerInterest(p -> {
            try
            {
                System.out.println("Listener");
                System.out.println(p);
                DecodedPacket decodedPacket = new DecodedPacket(p);
                System.out.println(decodedPacket);
                //BatterySegment s = decodedPacket.getSegments().toArray(new BatterySegment[1])[0];
                System.out.println(decodedPacket);
                pm.store(decodedPacket);
            } catch (InvalidSegmentException | PacketException | InvalidBlockException e)
            {
                e.printStackTrace();
            }
        });
    }
}
