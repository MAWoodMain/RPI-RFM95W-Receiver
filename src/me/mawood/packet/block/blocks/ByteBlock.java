package me.mawood.packet.block.blocks;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;

public class ByteBlock extends Block<Byte>
{
    public static final byte[] TYPE_FLAG = {0x01};

    public ByteBlock(byte[] data) throws InvalidBlockException
    {
        super(data);
    }

    @Override
    public Byte getData()
    {
        return data[0];
    }

    @Override
    public String toString()
    {
        return "ByteBlock{" +
                "value=" + String.format("0x%02X", data[0]) +
                '}';
    }
}
