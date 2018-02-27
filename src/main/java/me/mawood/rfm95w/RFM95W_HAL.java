package me.mawood.rfm95w;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import static me.mawood.Logging.*;

public class RFM95W_HAL implements GpioPinListenerDigital
{
    private final SpiDevice spi;
    private final GpioPinDigitalOutput reset;
    private final GpioPinDigitalInput dio0;
    private ArrayList<GpioPinListenerDigital> dioListeners = null;

    protected RFM95W_HAL() throws IOException
    {
        entering();
        SpiDevice spi = SpiFactory.getInstance(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED/2, // default spi speed 1 MHz
                SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0
        this.spi = spi;

        final GpioController gpio = GpioFactory.getInstance();
        reset = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "rst", PinState.LOW);
        dio0 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, "dio0");
        dio0.setPullResistance(PinPullResistance.PULL_DOWN);
        exiting();
    }

    protected byte readRegister(byte register) throws IOException
    {
        entering();
        logger.log(Level.FINER, "Reading register {0}", String.format("0x%02X", register));
        byte[] data = {(byte)(register & 0x7F),(byte)0x00};
        byte[] response = spi.write(data);
        logger.log(Level.FINER, "Read {0}", String.format("0x%02X from 0x%02X",response[1], register));
        exiting();
        return response[1];
    }

    protected void writeRegister(byte register, byte value) throws IOException
    {
        entering();
        logger.log(Level.FINER, "Writing {0}", String.format("0x%02X to 0x%02X", value, register));
        byte[] data = {(byte)(register | 0x80), value};
        spi.write(data);
        exiting();
    }

    protected void reset()
    {
        entering();
        logger.log(Level.FINER, "Resetting RFM95W");
        reset.low();
        try
        {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}
        reset.high();
        try
        {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}
        logger.log(Level.FINER, "RFM95W has been reset");
        exiting();
    }

    protected boolean isDio0High()
    {
        entering();
        exiting();
        return dio0.isHigh();
    }

    protected void registerInterestInDio0(GpioPinListenerDigital listener)
    {
        entering();
        // if first new listener
        if(dioListeners == null)
        {
            // setup listener collection
            dioListeners = new ArrayList<>();
            // start listening to the gpio
            dio0.addListener(this);
        }
        dioListeners.add(listener);
        exiting();
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event)
    {
        entering();
        dioListeners.forEach(e -> e.handleGpioPinDigitalStateChangeEvent(event));
        exiting();
    }

    public SpiDevice getSpi()
    {
        return spi;
    }
}
