package me.mawood.rfm95w.packet.block.blocks;

import me.mawood.rfm95w.packet.block.InvalidBlockException;
import me.mawood.rfm95w.packet.block.Block;

import java.nio.ByteBuffer;

/**
 * 4 byte integer block
 */
public class IntBlock extends Block<Integer>
{
    public static final byte[] TYPE_FLAG = {0x03};
    private final int value;

    public IntBlock(byte[] data) throws InvalidBlockException
    {
        super(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        value = buffer.getInt();
    }

    @Override
    public Integer getData()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "IntBlock{" +
                "value=" + value +
                '}';
    }

    public static IntBlock fromData(int value)
    {
        try
        {
            return new IntBlock(ByteBuffer.allocate(4).putInt(value).array());
        } catch (InvalidBlockException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getBytes()
    {
        ByteBuffer buffer = ByteBuffer.allocate(6);
        buffer.put(TYPE_FLAG);
        buffer.put((byte) 0x04);
        buffer.put(data);
        return buffer.array();
    }
}
