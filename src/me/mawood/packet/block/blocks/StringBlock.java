package me.mawood.packet.block.blocks;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;

import java.util.Arrays;

public class StringBlock extends Block<String>
{
    public static final byte[] TYPE_FLAG = {0x10};

    private final String value;

    public StringBlock(byte[] data) throws InvalidBlockException
    {
        super(data);
        value = new String(data);
    }

    @Override
    public String getData()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "StringBlock{" +
                "value='" + value + '\'' +
                '}';
    }
}
