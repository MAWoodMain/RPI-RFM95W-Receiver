package me.mawood.packet.block.blocks;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;

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
}
