package me.mawood.loraCapture.spark;

import me.mawood.loraCapture.persistence.loRaPacket.LoRaPacket;

public interface PacketListener
{
    void handlePacket(LoRaPacket packet);
}
