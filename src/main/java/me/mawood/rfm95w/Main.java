package me.mawood.rfm95w;

import me.mawood.rfm95w.mqtt.MqttPacketBuilder;
import me.mawood.rfm95w.packet.PacketException;
import me.mawood.rfm95w.packet.PacketStreamReader;
import me.mawood.rfm95w.packet.block.InvalidBlockException;
import me.mawood.rfm95w.packet.segment.InvalidSegmentException;
import me.mawood.rfm95w.radio.RFM95W;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Arrays;
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
        Logging.entering();

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(key.toCharArray());

        client = new MqttClient(
                "tcp://io.adafruit.com:1883", //URI
                MqttClient.generateClientId(), //ClientId
                new MemoryPersistence()); //Persistence
        client.connect(options);
        while(!client.isConnected())
        {
            Logging.logger.log(Level.INFO, "Waiting for MQTT to connect");
            Thread.sleep(1000);
        }
        RFM95W rfm95w = new RFM95W();
        rfm95w.registerInterestInMessages(m -> {
            try
            {
                Logging.logger.log(Level.FINE, m.toString());
                Logging.logger.log(Level.INFO, Arrays.toString(Arrays.stream(new PacketStreamReader((m.getMessage())).getSegments()).map(s -> s.toJson().toJSONString()).toArray()));

                if(client.isConnected())
                {
                    client.publish(username + "/f/unicorn", new MqttPacketBuilder(new PacketStreamReader(m.getMessage()).getSegments()).getMessage());
                } else
                {
                    Logging.logger.log(Level.WARNING, "Failed to publish packet, MQTT not connected");
                }


            } catch (PacketException | InvalidBlockException | InvalidSegmentException e)
            {
                if(e.getCause() != null)
                {
                    Logging.logger.log(Level.WARNING, "Invalid packet received error: " + e.getCause().getMessage());
                } else
                {
                    Logging.logger.log(Level.WARNING, "Invalid packet received error: " + e.getMessage());
                }
            } catch (MqttException e)
            {
                Logging.logger.log(Level.WARNING, "Failed to publish packet, MQTT error " + e.getMessage());
            }
        });
        client.disconnect();

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
        Logging.exiting();
    }
}
