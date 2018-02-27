package me.mawood.rfm95w.radio.registers;

import com.pi4j.io.spi.SpiDevice;
import me.mawood.registers.BitMask;
import me.mawood.registers.Register;
import me.mawood.registers.interfaces.SPIRegisterInterface;

import java.util.EnumSet;

public class ModemConfig2 extends Register
{
    public enum ModemConfig2Masks implements BitMask
    {
        SF_6(0x06<<4),
        SF_7(0x07<<4),
        SF_8(0x08<<4),
        SF_9(0x09<<4),
        SF_10(0x0A<<4),
        SF_11(0x0B<<4),
        SF_12(0x0C<<4),

        TX_NORMAL_MODE(0x00<<3),
        TX_CONTINUOUS_MODE(0x01<<3),

        RX_PAYLOAD_CRC_OFF(0x00<<2),
        RX_PAYLOAD_CRC_ON(0x01<<2);

        final byte mask;

        ModemConfig2Masks(int mask)
        {
            this.mask = (byte) mask;
        }

        @Override
        public byte getMask()
        {
            return mask;
        }
    }

    public ModemConfig2 (SpiDevice device)
    {
        super(new SPIRegisterInterface(device), (byte) 0x1E,
                Register.enumSetToSet(EnumSet.of(
                        ModemConfig2Masks.SF_8,
                        ModemConfig2Masks.TX_NORMAL_MODE,
                        ModemConfig2Masks.RX_PAYLOAD_CRC_ON)));
    }
}
