package me.mawood.rfm95w.registers;

import java.util.EnumSet;

public enum PaRamp
{
    PR_3_4MS(0x00,0x01),
    PR_2MS(0x01,0x01),
    PR_1MS(0x02,0x01),
    PR_500US(0x03,0x01),
    PR_250US(0x04,0x01),
    PR_125US(0x05,0x01),
    PR_100US(0x06,0x01),
    PR_62US(0x07,0x01),
    PR_50US(0x08,0x01),
    PR_40US(0x09,0x01),
    PR_31US(0x0A,0x01),
    PR_25US(0x0B,0x01),
    PR_20US(0x0C,0x01),
    PR_15US(0x0D,0x01),
    PR_12US(0x0E,0x01),
    PR_10US(0x0F,0x01);

    final byte mask;
    final byte set;
    PaRamp(int mask, int set)
    {
        this.mask = (byte) mask;
        this.set  = (byte) set;
    }

    public static boolean isValidSet(EnumSet<PaRamp> set)
    {
        return set.size() == set.stream().mapToInt(r ->r.set).distinct().count();
    }

    public static byte getRegister(EnumSet<PaRamp> set) throws InvalidRegisterConfigurationException
    {
        if(!isValidSet(set)) throw new InvalidRegisterConfigurationException("Incompatible register combination");
        byte reg = 0x00;
        for(PaRamp config:set) reg |= config.mask;
        return reg;
    }
}
