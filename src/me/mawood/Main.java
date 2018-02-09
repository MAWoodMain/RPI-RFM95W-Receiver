package me.mawood;

import static me.mawood.Logging.*;
import me.mawood.rfm95w.RFM95W;

import java.util.logging.Level;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        entering();
        RFM95W rfm95w = new RFM95W();
        rfm95w.registerInterestInMessages(m -> logger.log(Level.INFO, m.toString()));

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
