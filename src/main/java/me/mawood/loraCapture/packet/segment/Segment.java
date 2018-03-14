package me.mawood.loraCapture.packet.segment;

import com.google.gson.JsonObject;
import me.mawood.loraCapture.packet.DecodedPacket;
import me.mawood.loraCapture.packet.block.Block;
import me.mawood.loraCapture.persistence.Persistable;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "segment")
public class Segment implements Persistable
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
    @Column(name = "id")
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
    public JsonObject toJson()
    {
        return new JsonObject();
    }

    @Transient
    public String getJsonName()
    {
        return "Segment";
    }

    protected int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="packetId", nullable=false)
    public DecodedPacket getPacket()
    {
        return packet;
    }

    public void setPacket(DecodedPacket packet)
    {
        this.packet = packet;
    }

    @Override
    public void prepare()
    {

    }
}
