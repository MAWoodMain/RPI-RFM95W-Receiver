package me.mawood.rfm95w.radio;

import me.mawood.registers.Register;
import me.mawood.rfm95w.Logging;
import me.mawood.rfm95w.radio.registers.InvalidRegisterConfigurationException;
import me.mawood.rfm95w.radio.registers.ModemConfig1;
import me.mawood.rfm95w.radio.registers.ModemConfig2;
import me.mawood.rfm95w.radio.registers.PaRamp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.logging.Level;

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

    private final Register modemConfig1;
    private final Register modemConfig2;
    private final Register paRamp;

    private final RFM95W_HAL hal;
    private final ArrayList<MessageReceivedListener> listeners;

    public RFM95W() throws IOException, InterruptedException, InvalidRegisterConfigurationException
    {
        Logging.entering();
        //logger.entering(this.getClass().getJsonName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        hal = new RFM95W_HAL();
        listeners = new ArrayList<>();

        modemConfig1 = new ModemConfig1(hal.getSpi());
        modemConfig2 = new ModemConfig2(hal.getSpi());
        paRamp = new PaRamp(hal.getSpi());


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
                    Logging.logger.log(Level.WARNING, "CRC failed, message dropped");
                }
            }
        });
        Logging.exiting();
    }

    private void setup() throws IOException, InterruptedException, InvalidRegisterConfigurationException
    {
        Logging.entering();
        hal.reset();
        byte version = hal.readRegister(RFM95W_Registers.REG_VERSION);
        if(version != (byte)0x12)
        {
            Logging.logger.log(Level.SEVERE, "Version register not recognised check connections");
            return;
        }
        Logging.logger.log(Level.INFO, "RFM9X recognised");
        setMode(OperatingMode.SX72_MODE_SLEEP);
        Thread.sleep(500);
        long frf = (FREQUENCY << 19) / 32000000L;
        hal.writeRegister(RFM95W_Registers.REG_FRF_MSB, (byte)(frf >>16));
        hal.writeRegister(RFM95W_Registers.REG_FRF_MID, (byte)(frf >> 8));
        hal.writeRegister(RFM95W_Registers.REG_FRF_LSB, (byte)(frf));

        modemConfig1.setMasks(Register.enumSetToSet(EnumSet.of(
                ModemConfig1.ModemConfig1Masks.BW_125KHZ,
                ModemConfig1.ModemConfig1Masks.CR_4_5,
                ModemConfig1.ModemConfig1Masks.EXPLICIT_HEADER_MODE)));
        modemConfig1.writeRegister();

        modemConfig2.setMasks(Register.enumSetToSet(EnumSet.of(
                ModemConfig2.ModemConfig2Masks.SF_8,
                ModemConfig2.ModemConfig2Masks.TX_NORMAL_MODE,
                ModemConfig2.ModemConfig2Masks.RX_PAYLOAD_CRC_ON)));
        modemConfig2.writeRegister();

        paRamp.setMasks(Register.enumSetToSet(EnumSet.of(
                PaRamp.PaRampMask.PR_50US,
                PaRamp.PaRampMask.MS_NO_SHAPING
        )));
        paRamp.writeRegister();

        hal.writeRegister(RFM95W_Registers.REG_PAYLOAD_LENGTH, PAYLOAD_LENGTH);

        hal.writeRegister(RFM95W_Registers.REG_SYNC_WORD, (byte)0x34); // LoRaWAN public sync word

        hal.writeRegister(RFM95W_Registers.REG_DIO_MAPPING_1, (byte)0x00);
        hal.writeRegister(RFM95W_Registers.REG_DIO_MAPPING_2, (byte)0x00);

        setMode(OperatingMode.SX72_MODE_STANDBY);

        hal.writeRegister(RFM95W_Registers.REG_FIFO_ADDR_PTR, (byte)0x00);//hal.readRegister(REG_FIFO_RX_BASE_AD)

        hal.writeRegister(RFM95W_Registers.REG_LR_PACONFIG, RFM95W_Registers.PA_OFF_BOOST);   // TURN PA OFF FOR RECIEVE?
        hal.writeRegister(RFM95W_Registers.REG_LNA, RFM95W_Registers.LNA_MAX_GAIN);        // MAX GAIN FOR RECIEVE
        setMode(OperatingMode.SX72_MODE_RX_CONTINUOUS);

        hal.writeRegister(RFM95W_Registers.REG_IRQ_FLAGS, (byte)0xff);

        Logging.logger.log(Level.INFO, "RFM95W setup complete");
        Logging.exiting();
    }

    private Message receivePkt() throws IOException, MessageInvalidException
    {
        Logging.entering();
        int irqFlags = hal.readRegister(RFM95W_Registers.REG_IRQ_FLAGS); // if any of these are set then the inbound message failed
        if(irqFlags != 0x00)
        {
            Logging.logger.log(Level.FINER, "IRQ: {0}", String.format("0x%02X", irqFlags));
        }

        // if rxdone flag set
        if((irqFlags & 0x40)>0)
        {
            // clear the rxDone flag
            hal.writeRegister(RFM95W_Registers.REG_IRQ_FLAGS, (byte)0xFF);

            // check for payload crc issues (0x20 is the bit we are looking for
            if((irqFlags & 0x20) == 0x20)
            {
                Logging.logger.log(Level.FINE, "CRC Error");
                // reset the crc flags
                hal.writeRegister(RFM95W_Registers.REG_IRQ_FLAGS, (byte)0x20);
                Logging.exiting();
                throw new MessageInvalidException("CRC invalid");
            }
            else{
                byte currentAddr = hal.readRegister(RFM95W_Registers.REG_FIFO_RX_CURRENT_ADDR);
                int receivedCount = hal.readRegister(RFM95W_Registers.REG_RX_NB_BYTES) & 0xff;
                int rssi = hal.readRegister(RFM95W_Registers.REG_LR_PKTRSSIVALUE);
                rssi -= 157;

                hal.writeRegister(RFM95W_Registers.REG_FIFO_ADDR_PTR, currentAddr);
                // now loop over the fifo getting the data
                byte[] message = new byte[receivedCount];
                for(int i = 0; i < receivedCount; i++)
                {
                    message[i] = hal.readRegister(RFM95W_Registers.REG_FIFO);
                }
                Logging.exiting();
                return new Message(message, rssi);
            }
        } else
        {
            Logging.exiting();
            throw new MessageInvalidException("Message not received");
        }
    }

    private void setMode(OperatingMode mode) throws IOException
    {
        Logging.entering();
        Logging.logger.log(Level.FINE, "Changing operating mode to " + mode.name());
        hal.writeRegister(RFM95W_Registers.REG_OPMODE, mode.value);
        Logging.exiting();
    }

    public void registerInterestInMessages(MessageReceivedListener listener)
    {
        Logging.entering();
        listeners.add(listener);
        Logging.exiting();
    }

    public byte readIRQ() throws IOException
    {
        return hal.readRegister(RFM95W_Registers.REG_IRQ_FLAGS);
    }
}
