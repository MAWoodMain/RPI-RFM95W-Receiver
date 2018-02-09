package me.mawood.packet.block;

import java.util.Arrays;

public enum Blocks
{
    INT_BLOCK(IntBlock.TYPE_FLAG, IntBlock.class);

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
