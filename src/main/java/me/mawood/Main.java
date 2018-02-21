package me.mawood;

import static me.mawood.Logging.*;

import me.mawood.packet.PacketException;
import me.mawood.packet.PacketStreamReader;
import me.mawood.packet.block.InvalidBlockException;
import me.mawood.packet.segment.InvalidSegmentException;
import me.mawood.rfm95w.RFM95W;
import me.mawood.rfm95w.registers.PaRamp;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumSet;
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
        entering();
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(115000);
        System.out.println(bytesToHex(bb.array()));

        System.out.printf("register: 0x%02X\n\r", PaRamp.getRegister(EnumSet.of(PaRamp.PR_50US)));
        RFM95W rfm95w = new RFM95W();
        rfm95w.registerInterestInMessages(m -> {
            try
            {
                logger.log(Level.FINE, m.toString());
                logger.log(Level.INFO, Arrays.toString(new PacketStreamReader((m.getMessage())).getSegments()));
            } catch (PacketException | InvalidBlockException | InvalidSegmentException e)
            {
                if(e.getCause() != null)
                {
                    logger.log(Level.WARNING, "Invalid packet received error: " + e.getCause().getMessage());
                } else
                {
                    logger.log(Level.WARNING, "Invalid packet received error: " + e.getMessage());
                }
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
