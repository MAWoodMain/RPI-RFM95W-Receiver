package me.mawood.rfm95w.registers;

public interface Register
{
    byte getMask();
    byte getSet();
    byte getNumberOfSets();
}
