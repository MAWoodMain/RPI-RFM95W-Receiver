package me.mawood.loraCapture.packet.segment;

import com.google.gson.JsonObject;
import me.mawood.loraCapture.packet.DecodedPacket;
import me.mawood.loraCapture.packet.block.Block;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "segment")
public abstract class Segment
{
    @Transient
    public static final byte SEGMENT_FLAG = (byte) 0xAA;
    @Transient
    public static final int TYPE_FLAG_LENGTH = 2;

    @Transient
    protected Block[] blocks;

    private DecodedPacket packet;

    public Segment(Block[] blocks)
    {
        this.blocks = blocks;
    }

    protected Segment()
    {
    }


    @Id
    @Column(name = "segmentId")
    @GeneratedValue
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Transient
    public int getLength()
    {
        return 1 + TYPE_FLAG_LENGTH + Arrays.stream(blocks).mapToInt(Block::getLength).sum();
    }

    @Transient
    public abstract JsonObject toJson();

    @Transient
    public abstract String getJsonName();

    protected int id;

    @ManyToOne
    @JoinColumn(name="packetId", nullable=false)
    public DecodedPacket getPacket()
    {
        return packet;
    }

    public void setPacket(DecodedPacket packet)
    {
        this.packet = packet;
    }
}
