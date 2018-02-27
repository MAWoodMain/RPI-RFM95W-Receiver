package me.mawood.rfm95w.packet.block;

public abstract class Block<T>
{
    public static final int TYPE_FLAG_LENGTH = 1;

    protected final byte[] data;

    public Block(byte[] data)
    {
        this.data = data;
    }

    public abstract T getData();

    public byte[] getDataRaw()
    {
        return data;
    }

    public int getLength()
    {
        return TYPE_FLAG_LENGTH + data.length;
    }
}
