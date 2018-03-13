package me.mawood.loraCapture;

import me.mawood.loraCapture.packet.PacketException;
import me.mawood.loraCapture.packet.PacketStreamReader;
import me.mawood.loraCapture.packet.block.InvalidBlockException;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.spark.CaptureEndpoint;

import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;

public class Main
{
    public static void main(String[] args)
    {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        CaptureEndpoint endpoint = new CaptureEndpoint();
        endpoint.registerInterest(System.out::println);
        endpoint.registerInterest(p -> {
            try
            {
                PacketStreamReader psr = new PacketStreamReader(Base64.getDecoder().decode(new String(p.getData()).getBytes()));
                System.out.println(Arrays.toString(psr.getSegments()));
            } catch (InvalidSegmentException | PacketException | InvalidBlockException e)
            {
                e.printStackTrace();
            }
        });
    }
}
