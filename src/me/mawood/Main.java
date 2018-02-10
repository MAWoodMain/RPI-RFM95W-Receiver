package me.mawood;

import static me.mawood.Logging.*;

import me.mawood.packet.PacketException;
import me.mawood.packet.PacketStreamReader;
import me.mawood.rfm95w.RFM95W;

import java.util.Arrays;
import java.util.logging.Level;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        entering();
        RFM95W rfm95w = new RFM95W();
        rfm95w.registerInterestInMessages(m -> {
            try
            {
                logger.log(Level.INFO, m.getHexString());
                logger.log(Level.INFO, Arrays.toString(new PacketStreamReader((m.getMessage())).getSegments()));
            } catch (PacketException e)
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
