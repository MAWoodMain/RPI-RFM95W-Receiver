package me.mawood.packet.block.blocks;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;

import java.nio.ByteBuffer;
/**
 * 4 byte float block
 */
public class FloatBlock extends Block<Float>
{
    public static final byte[] TYPE_FLAG = {0x05};
    private final float value;

    public FloatBlock(byte[] data) throws InvalidBlockException
    {
        super(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        value = buffer.getFloat();
    }

    @Override
    public Float getData()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "FloatBlock{" +
                "value=" + value +
                '}';
    }
}
