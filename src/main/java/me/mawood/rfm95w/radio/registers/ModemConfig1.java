package me.mawood.rfm95w.radio.registers;

import com.pi4j.io.spi.SpiDevice;
import me.mawood.registers.BitMask;
import me.mawood.registers.Register;
import me.mawood.registers.interfaces.SPIRegisterInterface;

import java.util.EnumSet;

public class ModemConfig1 extends Register
{

    public enum ModemConfig1Masks implements BitMask
    {
        BW_7_8KHZ(0x00<<4),
        BW_10_4KHZ(0x01<<4),
        BW_15_6KHZ(0x02<<4),
        BW_20_8KHZ(0x03<<4),
        BW_31_25KHZ(0x04<<4),
        BW_41_7KHZ(0x05<<4),
        BW_62_5KHZ(0x06<<4),
        BW_125KHZ(0x07<<4),
        BW_250KHZ(0x08<<4),
        BW_500KHZ(0x09<<4),

        CR_4_5(0x01<<1),
        CR_4_6(0x02<<1),
        CR_4_7(0x03<<1),
        CR_4_8(0x04<<1),

        EXPLICIT_HEADER_MODE(0x00),
        IMPLICIT_HEADER_MODE(0x01);

        final byte mask;
        ModemConfig1Masks(int mask)
        {
            this.mask = (byte) mask;
        }
        @Override
        public byte getMask()
        {
            return mask;
        }
    }

    public ModemConfig1(SpiDevice device)
    {
        super(new SPIRegisterInterface(device), (byte) 0x1D,
                Register.enumSetToSet(EnumSet.of(
                        ModemConfig1Masks.BW_7_8KHZ,
                        ModemConfig1Masks.CR_4_5,
                        ModemConfig1Masks.EXPLICIT_HEADER_MODE)));
    }
}
