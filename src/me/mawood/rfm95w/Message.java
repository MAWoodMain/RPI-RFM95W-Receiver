package me.mawood.rfm95w;

public class Message
{
    private final byte[] message;
    private final int RSSI;

    public Message(byte[] message, int RSSI)
    {
        this.message = message;
        this.RSSI = RSSI;
    }

    public byte[] getMessage()
    {
        return message;
    }

    public int getRSSI()
    {
        return RSSI;
    }

    public String getMessageString()
    {
        return new String(message);
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "message=" + getMessageString() +
                ", RSSI=" + RSSI +
                '}';
    }
}
