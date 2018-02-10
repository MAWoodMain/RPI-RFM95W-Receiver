package me.mawood;

import static me.mawood.Logging.*;

import me.mawood.packet.PacketException;
import me.mawood.packet.PacketStreamReader;
import me.mawood.rfm95w.RFM95W;
import me.mawood.rfm95w.registers.ModemConfig1;
import me.mawood.rfm95w.registers.ModemConfig2;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.logging.Level;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        entering();
        System.out.printf("register: 0x%02X\n\r", ModemConfig2.getRegister(EnumSet.of(ModemConfig2.SF_8, ModemConfig2.TX_NORMAL_MODE, ModemConfig2.RX_PAYLOAD_CRC_ON)));
        RFM95W rfm95w = new RFM95W();
        rfm95w.registerInterestInMessages(m -> {
            try
            {
                logger.log(Level.INFO, m.toString());
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
