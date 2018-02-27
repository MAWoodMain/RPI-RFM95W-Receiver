package me.mawood.rfm95w.radio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import me.mawood.rfm95w.Logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class RFM95W_HAL implements GpioPinListenerDigital
{
    private final SpiDevice spi;
    private final GpioPinDigitalOutput reset;
    private final GpioPinDigitalInput dio0;
    private ArrayList<GpioPinListenerDigital> dioListeners = null;

    protected RFM95W_HAL() throws IOException
    {
        Logging.entering();
        SpiDevice spi = SpiFactory.getInstance(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED/2, // default spi speed 1 MHz
                SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0
        this.spi = spi;

        final GpioController gpio = GpioFactory.getInstance();
        reset = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "rst", PinState.LOW);
        dio0 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, "dio0");
        dio0.setPullResistance(PinPullResistance.PULL_DOWN);
        Logging.exiting();
    }

    protected byte readRegister(byte register) throws IOException
    {
        Logging.entering();
        Logging.logger.log(Level.FINER, "Reading register {0}", String.format("0x%02X", register));
        byte[] data = {(byte)(register & 0x7F),(byte)0x00};
        byte[] response = spi.write(data);
        Logging.logger.log(Level.FINER, "Read {0}", String.format("0x%02X from 0x%02X",response[1], register));
        Logging.exiting();
        return response[1];
    }

    protected void writeRegister(byte register, byte value) throws IOException
    {
        Logging.entering();
        Logging.logger.log(Level.FINER, "Writing {0}", String.format("0x%02X to 0x%02X", value, register));
        byte[] data = {(byte)(register | 0x80), value};
        spi.write(data);
        Logging.exiting();
    }

    protected void reset()
    {
        Logging.entering();
        Logging.logger.log(Level.FINER, "Resetting RFM95W");
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
        Logging.logger.log(Level.FINER, "RFM95W has been reset");
        Logging.exiting();
    }

    protected boolean isDio0High()
    {
        Logging.entering();
        Logging.exiting();
        return dio0.isHigh();
    }

    protected void registerInterestInDio0(GpioPinListenerDigital listener)
    {
        Logging.entering();
        // if first new listener
        if(dioListeners == null)
        {
            // setup listener collection
            dioListeners = new ArrayList<>();
            // start listening to the gpio
            dio0.addListener(this);
        }
        dioListeners.add(listener);
        Logging.exiting();
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event)
    {
        Logging.entering();
        dioListeners.forEach(e -> e.handleGpioPinDigitalStateChangeEvent(event));
        Logging.exiting();
    }

    public SpiDevice getSpi()
    {
        return spi;
    }
}
