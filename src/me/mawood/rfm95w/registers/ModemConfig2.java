package me.mawood.rfm95w.registers;

import java.util.EnumSet;

public enum ModemConfig2
{
    SF_6(0x06<<4,0x01),
    SF_7(0x07<<4,0x01),
    SF_8(0x08<<4,0x01),
    SF_9(0x09<<4,0x01),
    SF_10(0x0A<<4,0x01),
    SF_11(0x0B<<4,0x01),
    SF_12(0x0C<<4,0x01),

    TX_NORMAL_MODE(0x00<<3, 0x02),
    TX_CONTINUOUS_MODE(0x01<<3, 0x02),

    RX_PAYLOAD_CRC_OFF(0x00<<2, 0x03),
    RX_PAYLOAD_CRC_ON(0x01<<2, 0x03);

    final byte mask;
    final byte set;
    ModemConfig2(int mask, int set)
    {
        this.mask = (byte) mask;
        this.set  = (byte) set;
    }

    public static boolean isValidSet(EnumSet<ModemConfig2> set)
    {
        return set.size() == set.stream().mapToInt(r ->r.set).distinct().count();
    }

    public static byte getRegister(EnumSet<ModemConfig2> set) throws InvalidRegisterConfigurationException
    {
        if(!isValidSet(set)) throw new InvalidRegisterConfigurationException("Incompatible register combination");
        byte reg = 0x00;
        for(ModemConfig2 config:set) reg |= config.mask;
        return reg;
    }

    public static byte getAddress()
    {
        return 0x1E;
    }
}
