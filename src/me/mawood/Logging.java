package me.mawood;

import java.util.logging.*;

public class Logging
{
    private static final Level LOGGING_LEVEL = Level.FINE;
    public static final Logger logger = Logger.getLogger("RFM95W");

    static
    {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        logger.setLevel(LOGGING_LEVEL);
        logger.setUseParentHandlers(false);
        for (Handler handler : logger.getHandlers()) {  logger.removeHandler(handler);}
        ConsoleHandler handler = new ConsoleHandler();
        //SimpleFormatter formatter = new SimpleFormatter();
        // handler = new StreamHandler(System.out, new SimpleFormatter());
        handler.setLevel(LOGGING_LEVEL);
        logger.addHandler(handler);
    }

    public static void entering()
    {
        logger.log(Level.FINEST, "ENTERING " + Thread.currentThread().getStackTrace()[2].getMethodName() + " from " + Thread.currentThread().getStackTrace()[2].getClassName());
        //logger.entering(Thread.currentThread().getStackTrace()[2].getClassName(), Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    public static void exiting()
    {
        logger.log(Level.FINEST, "EXITING " + Thread.currentThread().getStackTrace()[2].getMethodName() + " from " + Thread.currentThread().getStackTrace()[2].getClassName());
        //logger.exiting(Thread.currentThread().getStackTrace()[2].getClassName(), Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}
