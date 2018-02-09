package me.mawood.packet;

import me.mawood.packet.block.InvalidBlockException;
import me.mawood.packet.block.PacketStreamReader;

import java.util.Arrays;

public class PacketTestMain
{
    public static void main(String[] args) throws InvalidBlockException
    {
        System.out.println(Arrays.toString(new PacketStreamReader(PacketStreamReader.bytesToStream(new byte[]{(byte) 0xAA, 0x00, 0x01, 0x01, 0x04, 0x00, 0x00, 0x00, 0x10, 0x01, 0x04, 0x00, 0x00, 0x00, 0x20})).getSegments()));
    }
}
