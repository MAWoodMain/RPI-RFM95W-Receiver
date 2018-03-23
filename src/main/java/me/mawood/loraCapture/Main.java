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
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
    private static final Logger LOG = Logger.getLogger("mawood.loraCapture");
    public static void main(String[] args)
    {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        java.util.logging.Logger.getLogger("spark.Spark").setLevel(Level.WARNING);
        LOG.setLevel(Level.FINEST);

        LOG.info("Starting");

        PersistenceManager pm = new PersistenceManager();
        CaptureEndpoint endpoint = new CaptureEndpoint(pm);
        endpoint.registerInterest(p->
        {
            LOG.info("Packet Received:");
            LOG.fine(p.toString());
        });
        endpoint.registerInterest(p -> {
            if(p.getApplicationName().equals("RobTheUnicorn"))
            {
                LOG.info("Unicorn packet");
                try
                {
                    DecodedPacket decodedPacket = new DecodedPacket(p);
                    System.out.println(decodedPacket);
                    pm.store(decodedPacket);
                    LOG.fine("Stored packet");
                } catch (InvalidSegmentException | PacketException | InvalidBlockException e)
                {
                    e.printStackTrace();
                }
            }
            else if(p.getApplicationName().equals("Rain-Gauge"))
            {
                LOG.info("Rain packet");
                try
                {
                    Session session = PersistenceManager.getSession();
                    Query q = session.createQuery("SELECT fCnt FROM RainMeasurement WHERE fCnt=" + p.getfCnt());
                    session.close();
                    if (q.list().size() > 0)
                    {
                        LOG.info("Packet replay skipping");
                        return;
                    }


                    PacketStreamReader psr = new PacketStreamReader(Base64.getDecoder().decode(p.getData().getBytes()));
                    Segment[] segments = psr.getSegments();
                    RainSegment rs = (RainSegment) segments[2];

                    short batteryLevel = ((BatterySegment) segments[1]).getBatteryLevel();
                    float temperature = rs.getTemperature();
                    short[] readings = rs.getRainMeasurements();

                    final int interval = 120;

                    int count = 0;
                    Set<RainMeasurement> measurements = new HashSet<>();
                    for(short reading:readings)
                    {
                        measurements.add(new RainMeasurement(p.getfCnt(), reading, Instant.parse(p.getRxInfo()[0].getTime()).minusSeconds(count*interval), temperature, batteryLevel));
                        count++;
                    }
                    measurements.stream().sorted(Comparator.comparing(RainMeasurement::getTimestamp)).forEach(m ->
                    {
                        LOG.fine(m.toString());
                        pm.store(m);
                    });
                    LOG.info("Stored data");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else
            {
                LOG.info("Unknown protocol '" + p.getApplicationName() + "'");
            }
        });
    }
}
