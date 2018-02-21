package me.mawood.rfm95w;

import me.mawood.packet.PacketException;

public interface MessageReceivedListener
{
    void handleMessage(Message message);
}
