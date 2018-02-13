package me.mawood;

import static me.mawood.Logging.*;

import me.mawood.packet.PacketException;
import me.mawood.packet.PacketStreamReader;
import me.mawood.packet.block.InvalidBlockException;
import me.mawood.packet.segment.InvalidSegmentException;
import me.mawood.rfm95w.RFM95W;
import me.mawood.rfm95w.registers.ModemConfig1;
import me.mawood.rfm95w.registers.ModemConfig2;
import me.mawood.rfm95w.registers.PaRamp;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Level;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        entering();
        System.out.printf("register: 0x%02X\n\r", PaRamp.getRegister(EnumSet.of(PaRamp.PR_50US)));
        RFM95W rfm95w = new RFM95W();
        rfm95w.registerInterestInMessages(m -> {
            try
            {
                logger.log(Level.INFO, m.toString());
                logger.log(Level.INFO, Arrays.toString(new PacketStreamReader((m.getMessage())).getSegments()));
            } catch (PacketException | InvalidBlockException | InvalidSegmentException e)
            {
                logger.log(Level.WARNING, "Invalid packet received error: " + e.getMessage());
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
