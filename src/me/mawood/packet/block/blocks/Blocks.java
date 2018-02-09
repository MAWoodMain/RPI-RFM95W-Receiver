package me.mawood.packet.block.blocks;

import me.mawood.packet.block.InvalidBlockException;

import java.util.Arrays;

public enum Blocks
{
    BLOB_BLOCK(BlobBlock.TYPE_FLAG, BlobBlock.class),       // 0x00

    BYTE_BLOCK(ByteBlock.TYPE_FLAG, ByteBlock.class),       // 0x01
    SHORT_BLOCK(ShortBlock.TYPE_FLAG, ShortBlock.class),    // 0x02
    INT_BLOCK(IntBlock.TYPE_FLAG, IntBlock.class),          // 0x03
    LONG_BLOCK(LongBlock.TYPE_FLAG, LongBlock.class),       // 0x04

    FLOAT_BLOCK(FloatBlock.TYPE_FLAG, FloatBlock.class),    // 0x05
    DOUBLE_BLOCK(DoubleBlock.TYPE_FLAG, DoubleBlock.class), // 0x06

    STRING_BLOCK(StringBlock.TYPE_FLAG, StringBlock.class); // 0x10

    final byte[] blockId;
    final Class blockClass;
    Blocks(byte[] blockId, Class blockClass1)
    {
        this.blockId = blockId;
        this.blockClass = blockClass1;
    }
    public static Class getBlockClass(byte[] id) throws InvalidBlockException
    {
        for(Blocks b:Blocks.values())
        {
            if (Arrays.equals(b.blockId, id)) return b.blockClass;
        }
        throw new InvalidBlockException(String.format("Unknown block id 0x%02X", id[0]));
    }
}
