package me.mawood.packet.block.blocks;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;

import java.nio.ByteBuffer;

/**
 * 4 byte integer block
 */
public class LongBlock extends Block<Long>
{
    public static final byte[] TYPE_FLAG = {0x04};
    private final Long value;

    public LongBlock(byte[] data) throws InvalidBlockException
    {
        super(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        value = buffer.getLong();
    }

    @Override
    public Long getData()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "LongBlock{" +
                "value=" + value +
                '}';
    }

    public static LongBlock fromData(long value)
    {
        try
        {
            return new LongBlock(ByteBuffer.allocate(8).putLong(value).array());
        } catch (InvalidBlockException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
