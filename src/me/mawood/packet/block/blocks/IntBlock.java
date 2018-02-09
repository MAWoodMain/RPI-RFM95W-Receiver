package me.mawood.packet.block.blocks;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;

import java.nio.ByteBuffer;

/**
 * 4 byte integer block
 */
public class IntBlock extends Block<Integer>
{
    public static final byte[] TYPE_FLAG = {0x01};
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

    public static byte[] encode(Integer value)
    {
        ByteBuffer block = ByteBuffer.allocate(TYPE_FLAG.length + 1 + 4); // 2 byte flag, 1 byte length, 4 byte fixed length data
        block.put(TYPE_FLAG);
        block.put((byte) 4);
        block.putInt(value);
        return block.array();
    }

    @Override
    public String toString()
    {
        return "IntBlock{" +
                "value=" + value +
                '}';
    }
}
