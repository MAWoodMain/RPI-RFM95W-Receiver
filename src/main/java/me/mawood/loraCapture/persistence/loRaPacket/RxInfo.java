package me.mawood.loraCapture.persistence.loRaPacket;

import me.mawood.loraCapture.persistence.Persistable;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rx_info")
public class RxInfo implements Comparable<RxInfo>, Persistable
{

    int rxInfoId;

    private String mac;
    private String time;
    private int rssi;
    private int loRaSNR;
    private String name;
    private double latitude;
    private double longitude;
    private int altitude;
    private Set<LoRaPacket> packets = new HashSet<>(0);

    public RxInfo(String mac, String time, int rssi, int loRaSNR, String name, double latitude, double longitude, int altitude)
    {
        this.mac = mac;
        this.time = time;
        this.rssi = rssi;
        this.loRaSNR = loRaSNR;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public RxInfo()
    {
    }


    @Id
    @Column(name = "rxInfoId")
    @GeneratedValue
    public int getRxInfoId()
    {
        return rxInfoId;
    }

    public void setRxInfoId(int rxInfoId)
    {
        this.rxInfoId = rxInfoId;
    }

    @Basic
    @Column(name = "mac")
    public String getMac()
    {
        return mac;
    }

    public void setMac(String mac)
    {
        this.mac = mac;
    }

    @Basic
    @Column(name = "time")
    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    @Basic
    @Column(name = "rssi")
    public int getRssi()
    {
        return rssi;
    }

    public void setRssi(int rssi)
    {
        this.rssi = rssi;
    }

    @Basic
    @Column(name = "loraSnr")
    public int getLoRaSNR()
    {
        return loRaSNR;
    }

    public void setLoRaSNR(int loRaSNR)
    {
        this.loRaSNR = loRaSNR;
    }

    @Basic
    @Column(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Basic
    @Column(name = "latitude")
    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude")
    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "altitude")
    public int getAltitude()
    {
        return altitude;
    }

    public void setAltitude(int altitude)
    {
        this.altitude = altitude;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "rxInfos")
    public Set<LoRaPacket> getPackets()
    {
        return packets;
    }

    public void setPackets(Set<LoRaPacket> packets)
    {
        this.packets = packets;
    }

    @Override
    public String toString()
    {
        return "RxInfo{" +
                "mac='" + mac + '\'' +
                ", time='" + time + '\'' +
                ", rssi=" + rssi +
                ", loRaSNR=" + loRaSNR +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                '}';
    }

    @Override
    public void prepare()
    {

    }

    @Override
    public int compareTo(RxInfo o)
    {
        return Comparator
                .comparing(RxInfo::getAltitude)
                .thenComparing(RxInfo::getLatitude)
                .thenComparing(RxInfo::getLongitude)
                .thenComparing(RxInfo::getName)
                .thenComparing(RxInfo::getLoRaSNR)
                .thenComparing(RxInfo::getRssi)
                .thenComparing(RxInfo::getMac)
                .thenComparing(RxInfo::getTime).compare(this, o);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(rxInfoId, mac, time, rssi, loRaSNR, name, latitude, longitude, altitude);
    }

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof RxInfo)) return false;
        return this.compareTo((RxInfo) o) == 0;
    }
}
