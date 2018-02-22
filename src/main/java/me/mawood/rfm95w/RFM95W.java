package me.mawood.rfm95w;

import me.mawood.rfm95w.registers.InvalidRegisterConfigurationException;
import me.mawood.rfm95w.registers.ModemConfig1;
import me.mawood.rfm95w.registers.ModemConfig2;
import me.mawood.rfm95w.registers.PaRamp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
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
    private static final byte PAYLOAD_LENGTH = (byte) 64;

    private final EnumSet<ModemConfig1> modemConfig1 = EnumSet.of(ModemConfig1.BW_125KHZ, ModemConfig1.CR_4_5, ModemConfig1.EXPLICIT_HEADER_MODE);
    private final EnumSet<ModemConfig2> modemConfig2 = EnumSet.of(ModemConfig2.SF_8, ModemConfig2.TX_NORMAL_MODE, ModemConfig2.RX_PAYLOAD_CRC_ON);
    private final EnumSet<PaRamp> paRamp = EnumSet.of(PaRamp.PR_50US, PaRamp.MS_NO_SHAPING);

    private final RFM95W_HAL hal;
    private final ArrayList<MessageReceivedListener> listeners;

    public RFM95W() throws IOException, InterruptedException, InvalidRegisterConfigurationException
    {
        entering();
        //logger.entering(this.getClass().getJsonName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        hal = new RFM95W_HAL();
        listeners = new ArrayList<>();
        setup();
        // debug print on message
        hal.registerInterestInDio0(event -> {
            if(event.getState().isHigh())
            {
                try
                {
                    Message message = receivePkt();
                    listeners.forEach(l -> l.handleMessage(message));
                } catch (IOException e)
                {
                    e.printStackTrace();
                } catch (MessageInvalidException e)
                {
                    logger.log(Level.WARNING, "CRC failed, message dropped");
                }
            }
        });
        exiting();
    }

    private void setup() throws IOException, InterruptedException, InvalidRegisterConfigurationException
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

        hal.writeRegister(ModemConfig1.getAddress(), ModemConfig1.getRegister(modemConfig1));
        hal.writeRegister(ModemConfig2.getAddress(), ModemConfig2.getRegister(modemConfig2));
        hal.writeRegister(PaRamp.getAddress(),PaRamp.getRegister(paRamp));
        hal.writeRegister(REG_PAYLOAD_LENGTH, PAYLOAD_LENGTH);

        hal.writeRegister(REG_SYNC_WORD, (byte)0x34); // LoRaWAN public sync word

        hal.writeRegister(REG_DIO_MAPPING_1, (byte)0x00);
        hal.writeRegister(REG_DIO_MAPPING_2, (byte)0x00);

        setMode(OperatingMode.SX72_MODE_STANDBY);

        hal.writeRegister(REG_FIFO_ADDR_PTR, (byte)0x00);//hal.readRegister(REG_FIFO_RX_BASE_AD)

        hal.writeRegister(REG_LR_PACONFIG, PA_OFF_BOOST);   // TURN PA OFF FOR RECIEVE?
        hal.writeRegister(REG_LNA, LNA_MAX_GAIN);        // MAX GAIN FOR RECIEVE
        setMode(OperatingMode.SX72_MODE_RX_CONTINUOUS);

        hal.writeRegister(REG_IRQ_FLAGS, (byte)0xff);

        logger.log(Level.INFO, "RFM95W setup complete");
        exiting();
    }

    private Message receivePkt() throws IOException, MessageInvalidException
    {
        entering();
        int irqFlags = hal.readRegister(REG_IRQ_FLAGS); // if any of these are set then the inbound message failed
        if(irqFlags != 0x00)
        {
            logger.log(Level.FINER, "IRQ: {0}", String.format("0x%02X", irqFlags));
        }

        // if rxdone flag set
        if((irqFlags & 0x40)>0)
        {
            // clear the rxDone flag
            hal.writeRegister(REG_IRQ_FLAGS, (byte)0xFF);

            // check for payload crc issues (0x20 is the bit we are looking for
            if((irqFlags & 0x20) == 0x20)
            {
                logger.log(Level.FINE, "CRC Error");
                // reset the crc flags
                hal.writeRegister(REG_IRQ_FLAGS, (byte)0x20);
                exiting();
                throw new MessageInvalidException("CRC invalid");
            }
            else{
                byte currentAddr = hal.readRegister(REG_FIFO_RX_CURRENT_ADDR);
                int receivedCount = hal.readRegister(REG_RX_NB_BYTES) & 0xff;
                int rssi = hal.readRegister(REG_LR_PKTRSSIVALUE);
                rssi -= 157;

                hal.writeRegister(REG_FIFO_ADDR_PTR, currentAddr);
                // now loop over the fifo getting the data
                byte[] message = new byte[receivedCount];
                for(int i = 0; i < receivedCount; i++)
                {
                    message[i] = hal.readRegister(REG_FIFO);
                }
                exiting();
                return new Message(message, rssi);
            }
        } else
        {
            exiting();
            throw new MessageInvalidException("Message not received");
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

    public byte readIRQ() throws IOException
    {
        return hal.readRegister(REG_IRQ_FLAGS);
    }
}
