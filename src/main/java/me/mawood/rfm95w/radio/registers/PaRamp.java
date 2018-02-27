package me.mawood.rfm95w.radio.registers;

import com.pi4j.io.spi.SpiDevice;
import me.mawood.registers.BitMask;
import me.mawood.registers.Register;
import me.mawood.registers.interfaces.SPIRegisterInterface;

import java.util.EnumSet;

public class PaRamp extends Register
{
    public enum PaRampMask implements BitMask
    {
        PR_3_4MS(0x00),
        PR_2MS(0x01),
        PR_1MS(0x02),
        PR_500US(0x03),
        PR_250US(0x04),
        PR_125US(0x05),
        PR_100US(0x06),
        PR_62US(0x07),
        PR_50US(0x08),
        PR_40US(0x09),
        PR_31US(0x0A),
        PR_25US(0x0B),
        PR_20US(0x0C),
        PR_15US(0x0D),
        PR_12US(0x0E),
        PR_10US(0x0F),

        MS_NO_SHAPING(0x00 << 5),
        MS_GF_1_OR_CUTOFF_BR(0x01 << 5),
        MS_GF_0_5_OR_CUTOFF_2BR(0x02 << 5),
        MS_GF_0_3(0x03 << 5);

        final byte mask;
        PaRampMask(int mask)
        {
            this.mask = (byte) mask;
        }

        public byte getMask()
        {
            return mask;
        }
    }

    public PaRamp (SpiDevice device)
    {
        super(new SPIRegisterInterface(device), (byte) 0x0A,
                Register.enumSetToSet(EnumSet.of(
                        ModemConfig2.ModemConfig2Masks.SF_8,
                        ModemConfig2.ModemConfig2Masks.TX_NORMAL_MODE,
                        ModemConfig2.ModemConfig2Masks.RX_PAYLOAD_CRC_ON)));
    }
}
