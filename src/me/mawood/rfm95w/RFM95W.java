package me.mawood.rfm95w;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import static me.mawood.Logging.*;
import static me.mawood.rfm95w.RFM95W_Registers.*;

public class RFM95W
{
    private enum OperatingMode
    {

        SX72_MODE_RX_CONTINUOUS     (0x85),
        SX72_MODE_TX                (0x83),
        SX72_MODE_SLEEP             (0x80),
        SX72_MODE_STANDBY           (0x81);

        public final byte value;
        OperatingMode(int value)
        {
            this.value = (byte)value;
        }
    }

    private static final long FREQUENCY = 868100000; // in Mhz! (868.1)
    private static final int SPREADING_FACTOR = 8;

    private final RFM95W_HAL hal;
    private final ArrayList<MessageReceivedListener> listeners;

    public RFM95W() throws IOException, InterruptedException
    {
        entering();
        //logger.entering(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        hal = new RFM95W_HAL();
        listeners = new ArrayList<>();
        setup();
        // debug print on message
        hal.registerInterestInDio0(event -> {
            if(event.getState().isHigh())
            {
                try
                {
                    byte[] message = receivePkt();
                    listeners.forEach(l -> l.handleMessage(message));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        exiting();
    }

    private void setup() throws IOException, InterruptedException
    {
        entering();
        hal.reset();
        byte version = hal.readRegister(REG_VERSION);
        if(version != (byte)0x12)
        {
            logger.log(Level.SEVERE, "Version register not recognised check connections");
            return;
        }
        logger.log(Level.INFO, "RFM9X recognised");
        setMode(OperatingMode.SX72_MODE_SLEEP);
        Thread.sleep(500);
        long frf = (FREQUENCY << 19) / 32000000L;
        hal.writeRegister(REG_FRF_MSB, (byte)(frf >>16));
        hal.writeRegister(REG_FRF_MID, (byte)(frf >> 8));
        hal.writeRegister(REG_FRF_LSB, (byte)(frf));

        hal.writeRegister(REG_SYNC_WORD, (byte)0x34); // LoRaWAN public sync word

        if (SPREADING_FACTOR == 11 || SPREADING_FACTOR == 12) {
            hal.writeRegister(REG_MODEM_CONFIG3,(byte)0x0C);
        } else {
            hal.writeRegister(REG_MODEM_CONFIG3,(byte)0x04);
        }

        hal.writeRegister(REG_MODEM_CONFIG, (byte)0x74);
        hal.writeRegister(REG_MODEM_CONFIG2,(byte)((SPREADING_FACTOR<<4) | 0x04));

        if (SPREADING_FACTOR == 10 || SPREADING_FACTOR == 11 || SPREADING_FACTOR == 12) {
            hal.writeRegister(REG_SYMB_TIMEOUT_LSB,(byte)0x05);
        } else {
            hal.writeRegister(REG_SYMB_TIMEOUT_LSB,(byte)0x08);
        }
        hal.writeRegister(REG_MAX_PAYLOAD_LENGTH,(byte)0x80);
        hal.writeRegister(REG_PAYLOAD_LENGTH, (byte)64);
        hal.writeRegister(REG_HOP_PERIOD,(byte)0xFF);
        hal.writeRegister(REG_FIFO_ADDR_PTR, hal.readRegister(REG_FIFO_RX_BASE_AD));

        hal.writeRegister(REG_LNA, LNA_MAX_GAIN);  // max lna gain
        Thread.sleep(500);
        setMode(OperatingMode.SX72_MODE_RX_CONTINUOUS);
        logger.log(Level.INFO, "RFM95W setup complete");
        exiting();
    }

    private byte[] receivePkt() throws IOException
    {
        entering();
        //if(!hal.isDio0High()) return new byte[0];

        // clear rxDone

        byte irqflags = hal.readRegister(REG_IRQ_FLAGS);
        hal.writeRegister(REG_IRQ_FLAGS, (byte)0xFF);

        logger.log(Level.FINE,"irq: %02x\n\r", irqflags);
        //  payload crc: 0x20
        if((irqflags & 0x20) == 0x20)
        {
            logger.log(Level.WARNING,"CRC error");
            hal.writeRegister(REG_IRQ_FLAGS, (byte)0x20);
            exiting();
            return new byte[0];
        } else {

            byte currentAddr = hal.readRegister(REG_FIFO_RX_CURRENT_ADDR);
            byte receivedCount = hal.readRegister(REG_RX_NB_BYTES);
            logger.log(Level.FINE, "Received count: %d\n\r", receivedCount);
            hal.writeRegister(REG_FIFO_ADDR_PTR, currentAddr);
            byte[] packet = new byte[receivedCount];
            for(int i = 0; i < receivedCount; i++)
            {
                packet[i] = hal.readRegister(REG_FIFO);
            }
            exiting();
            return packet;
        }
    }

    private void setMode(OperatingMode mode) throws IOException
    {
        entering();
        logger.log(Level.FINE, "Changing operating mode to " + mode.name());
        hal.writeRegister(REG_OPMODE, mode.value);
        exiting();
    }

    public void registerInterestInMessages(MessageReceivedListener listener)
    {
        entering();
        listeners.add(listener);
        exiting();
    }
}
