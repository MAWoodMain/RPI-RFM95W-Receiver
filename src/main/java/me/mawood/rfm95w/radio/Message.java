package me.mawood.rfm95w.radio;

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

    public String getHexString()
    {
        return bytesToHex(message);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "message=" + getHexString() +
                ", RSSI=" + RSSI +
                '}';
    }
}
