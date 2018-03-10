package me.mawood.rfm95w;

import me.mawood.rfm95w.packet.PacketException;
import me.mawood.rfm95w.packet.PacketStreamReader;
import me.mawood.rfm95w.packet.block.InvalidBlockException;
import me.mawood.rfm95w.packet.segment.InvalidSegmentException;
import me.mawood.rfm95w.radio.RFM95W;

import java.util.Arrays;
import java.util.logging.Level;

public class Main
{
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
        Logging.entering();
        RFM95W rfm95w = new RFM95W();
        rfm95w.registerInterestInMessages(m -> {
            try
            {
                Logging.logger.log(Level.FINE, m.toString());
                Logging.logger.log(Level.INFO, Arrays.toString(Arrays.stream(new PacketStreamReader((m.getMessage())).getSegments()).map(s -> s.toJson().toString()).toArray()));


            } catch (PacketException | InvalidBlockException | InvalidSegmentException e)
            {
                if(e.getCause() != null)
                {
                    Logging.logger.log(Level.WARNING, "Invalid LoRaPacket received error: " + e.getCause().getMessage());
                } else
                {
                    Logging.logger.log(Level.WARNING, "Invalid LoRaPacket received error: " + e.getMessage());
                }
            } /*catch (MqttException e)
            {
                Logging.logger.log(Level.WARNING, "Failed to publish LoRaPacket, MQTT error " + e.getMessage());
            }*/
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
        Logging.exiting();
    }
}
