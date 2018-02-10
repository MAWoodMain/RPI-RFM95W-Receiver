package me.mawood.packet;

import me.mawood.packet.block.InvalidBlockException;
import me.mawood.packet.block.blocks.DoubleBlock;

import java.util.Arrays;

public class PacketTestMain
{
    public static void main(String[] args) throws InvalidBlockException
    {
        byte[] data = hexStringToByteArray(
                "AA0001" +
                "0608404977AF4ADBC665" +
                "0608BFF6574927913E81" +
                "0608403e000000000000" +
                "AA000202020120");
        System.out.println(Arrays.toString(new PacketStreamReader(PacketStreamReader.bytesToStream(data)).getSegments()));
        System.out.println(bytesToHex(DoubleBlock.fromData(30.0f).getBytes()));
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
