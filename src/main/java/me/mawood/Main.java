package me.mawood;

import static me.mawood.Logging.*;

import me.mawood.mqtt.MqttPacketBuilder;
import me.mawood.packet.PacketException;
import me.mawood.packet.PacketStreamReader;
import me.mawood.packet.block.InvalidBlockException;
import me.mawood.packet.segment.InvalidSegmentException;
import me.mawood.rfm95w.RFM95W;
import me.mawood.rfm95w.registers.PaRamp;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Level;

public class Main
{
    private static MqttClient client;
    private static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void main(String[] args) throws Exception
    {
        if(args.length < 2)
        {
            System.err.println("Requires username and adafruit io key to run");
        }
        String username = args[0];
        String key = args[1];
        entering();

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(key.toCharArray());


        RFM95W rfm95w = new RFM95W();
        rfm95w.registerInterestInMessages(m -> {
            try
            {
                logger.log(Level.FINE, m.toString());
                logger.log(Level.INFO, Arrays.toString(Arrays.stream(new PacketStreamReader((m.getMessage())).getSegments()).map(s -> s.toJson().toJSONString()).toArray()));
                client = new MqttClient(
                        "tcp://io.adafruit.com:1883", //URI
                        MqttClient.generateClientId(), //ClientId
                        new MemoryPersistence()); //Persistence
                client.connect(options);
                client.publish(username + "/f/unicorn", new MqttPacketBuilder(new PacketStreamReader(m.getMessage()).getSegments()).getMessage());
                client.disconnect();

            } catch (PacketException | InvalidBlockException | InvalidSegmentException e)
            {
                if(e.getCause() != null)
                {
                    logger.log(Level.WARNING, "Invalid packet received error: " + e.getCause().getMessage());
                } else
                {
                    logger.log(Level.WARNING, "Invalid packet received error: " + e.getMessage());
                }
            } catch (MqttException e)
            {
                e.printStackTrace();
            }
        });

        try
        {
            while(true)
            {
                Thread.sleep(100);
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        exiting();
    }
}
