package me.mawood.loraCapture;

import me.mawood.loraCapture.packet.DecodedPacket;
import me.mawood.loraCapture.packet.PacketException;
import me.mawood.loraCapture.packet.PacketStreamReader;
import me.mawood.loraCapture.packet.block.InvalidBlockException;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.packet.segment.Segment;
import me.mawood.loraCapture.packet.segment.segments.BatterySegment;
import me.mawood.loraCapture.packet.segment.segments.RainSegment;
import me.mawood.loraCapture.persistence.PersistenceManager;
import me.mawood.loraCapture.persistence.RainMeasurement;
import me.mawood.loraCapture.spark.CaptureEndpoint;

import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
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
            if(p.getApplicationName().equals("RobTheUnicorn"))
            {
                try
                {
                    DecodedPacket decodedPacket = new DecodedPacket(p);
                    System.out.println(decodedPacket);
                    pm.store(decodedPacket);
                    System.out.println("Stored");
                } catch (InvalidSegmentException | PacketException | InvalidBlockException e)
                {
                    e.printStackTrace();
                }
            }
            else //if(p.getApplicationName().equals("RobTheUnicorn"))
            {
                try
                {
                    PacketStreamReader psr = new PacketStreamReader(Base64.getDecoder().decode(p.getData().getBytes()));
                    Segment[] segments = psr.getSegments();
                    RainSegment rs = (RainSegment) segments[2];

                    short batteryLevel = ((BatterySegment) segments[1]).getBatteryLevel();
                    float temperature = rs.getTemperature();
                    short[] readings = rs.getRainMeasurements();

                    int count = readings.length;
                    final int interval = 120;

                    Set<RainMeasurement> measurements = new HashSet<>();
                    for(short reading:readings)
                    {
                        count--;
                        measurements.add(new RainMeasurement(reading, Instant.parse(p.getRxInfo()[0].getTime()).minusSeconds(count*interval), temperature, batteryLevel));
                    }
                    System.out.println(measurements);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
