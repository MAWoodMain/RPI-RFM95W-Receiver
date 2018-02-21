package me.mawood.packet.block.blocks;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;

import java.nio.ByteBuffer;

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

    public static ByteBlock fromData(byte value)
    {
        try
        {
            return new ByteBlock(new byte[]{value});
        } catch (InvalidBlockException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getBytes()
    {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put(TYPE_FLAG);
        buffer.put((byte) 0x01);
        buffer.put(data);
        return buffer.array();
    }
}
