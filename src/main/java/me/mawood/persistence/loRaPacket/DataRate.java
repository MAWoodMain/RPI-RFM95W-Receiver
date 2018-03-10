package me.mawood.persistence.loRaPacket;

import me.mawood.persistence.Persistable;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Objects;

@Entity
@Table(name = "data_rate", schema = "data")
public class DataRate implements Comparable<DataRate>, Persistable
{
    private int dataRateId;
    private String modulation;
    private int bandwidth;
    private int spreadFactor;

    public DataRate(String modulation, int bandwidth, int spreadFactor)
    {
        this.modulation = modulation;
        this.bandwidth = bandwidth;
        this.spreadFactor = spreadFactor;
    }

    public DataRate()
    {
    }

    @Id
    @GeneratedValue
    @Column(name = "dataRateId")
    public int getDataRateId()
    {
        return dataRateId;
    }

    public void setDataRateId(int dataRateId)
    {
        this.dataRateId = dataRateId;
    }

    @Basic
    @Column(name = "modulation")
    public String getModulation()
    {
        return modulation;
    }

    public void setModulation(String modulation)
    {
        this.modulation = modulation;
    }

    @Basic
    @Column(name = "bandwidth")
    public int getBandwidth()
    {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth)
    {
        this.bandwidth = bandwidth;
    }

    @Basic
    @Column(name = "spreadFactor")
    public int getSpreadFactor()
    {
        return spreadFactor;
    }

    public void setSpreadFactor(int spreadFactor)
    {
        this.spreadFactor = spreadFactor;
    }

    @Override
    public String toString()
    {
        return "DataRate{" +
                "modulation='" + modulation + '\'' +
                ", bandwidth=" + bandwidth +
                ", spreadFactor=" + spreadFactor +
                '}';
    }

    @Override
    public int compareTo(DataRate o)
    {
        return Comparator
                .comparing(DataRate::getBandwidth)
                .thenComparing(DataRate::getModulation)
                .thenComparing(DataRate::getSpreadFactor).compare(this, o);
    }

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof DataRate)) return false;
        return this.compareTo((DataRate) o) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(dataRateId, modulation, bandwidth, spreadFactor);
    }

    @Override
    public void prepare()
    {

    }
}
