package me.mawood.packet.block.blocks;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;

import java.nio.ByteBuffer;

/**
 * 4 byte float block
 */
public class DoubleBlock extends Block<Double>
{
    public static final byte[] TYPE_FLAG = {0x06};
    private final double value;

    public DoubleBlock(byte[] data) throws InvalidBlockException
    {
        super(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        value = buffer.getDouble();
    }

    @Override
    public Double getData()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "DoubleBlock{" +
                "value=" + value +
                '}';
    }
}
