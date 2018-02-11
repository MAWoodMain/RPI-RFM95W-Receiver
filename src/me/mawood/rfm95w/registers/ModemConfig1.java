package me.mawood.rfm95w.registers;

import java.util.EnumSet;

public enum ModemConfig1
{
    BW_7_8KHZ(0x00<<4,0x01),
    BW_10_4KHZ(0x01<<4,0x01),
    BW_15_6KHZ(0x02<<4,0x01),
    BW_20_8KHZ(0x03<<4,0x01),
    BW_31_25KHZ(0x04<<4,0x01),
    BW_41_7KHZ(0x05<<4,0x01),
    BW_62_5KHZ(0x06<<4,0x01),
    BW_125KHZ(0x07<<4,0x01),
    BW_250KHZ(0x08<<4,0x01),
    BW_500KHZ(0x09<<4,0x01),

    CR_4_5(0x01<<1, 0x02),
    CR_4_6(0x02<<1, 0x02),
    CR_4_7(0x03<<1, 0x02),
    CR_4_8(0x04<<1, 0x02),

    EXPLICIT_HEADER_MODE(0x00, 0x03),
    IMPLICIT_HEADER_MODE(0x01, 0x03);

    final byte mask;
    final byte set;
    ModemConfig1(int mask, int set)
    {
        this.mask = (byte) mask;
        this.set  = (byte) set;
    }

    public static boolean isValidSet(EnumSet<ModemConfig1> set)
    {
        return set.size() == set.stream().mapToInt(r ->r.set).distinct().count();
    }

    public static byte getRegister(EnumSet<ModemConfig1> set) throws InvalidRegisterConfigurationException
    {
        if(!isValidSet(set)) throw new InvalidRegisterConfigurationException("Incompatible register combination");
        byte reg = 0x00;
        for(ModemConfig1 config:set) reg |= config.mask;
        return reg;
    }

    public static byte getAddress()
    {
        return 0x1D;
    }
}
