package me.mawood.rfm95w.packet.block.blocks;

import me.mawood.rfm95w.packet.block.Block;
import me.mawood.rfm95w.packet.block.InvalidBlockException;

import java.nio.ByteBuffer;

/**
 * 4 byte integer block
 */
public class ShortBlock extends Block<Short>
{
    public static final byte[] TYPE_FLAG = {0x02};
    private final short value;

    public ShortBlock(byte[] data) throws InvalidBlockException
    {
        super(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        value = buffer.getShort();
    }

    @Override
    public Short getData()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "ShortBlock{" +
                "value=" + value +
                '}';
    }

    public static ShortBlock fromData(short value)
    {
        try
        {
            return new ShortBlock(ByteBuffer.allocate(2).putShort(value).array());
        } catch (InvalidBlockException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getBytes()
    {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(TYPE_FLAG);
        buffer.put((byte) 0x02);
        buffer.put(data);
        return buffer.array();
    }
}
