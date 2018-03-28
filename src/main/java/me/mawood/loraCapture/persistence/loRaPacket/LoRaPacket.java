package me.mawood.loraCapture.persistence.loRaPacket;


import me.mawood.loraCapture.persistence.Persistable;
import me.mawood.loraCapture.persistence.PersistenceManager;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Entity
@Table(name = "packet")
public class LoRaPacket implements Persistable
{
    int packetId;

    String applicationID;
    String applicationName;
    String nodeName;
    String devEUI;
    private Set<RxInfo> rxInfos = new HashSet<>(0);
    TxInfo txInfo;
    int fCnt;
    int fPort;
    String data;

    @Transient
    public RxInfo[] getRxInfo()
    {
        return rxInfo;
    }

    public void setRxInfo(RxInfo[] rxInfo)
    {
        this.rxInfo = rxInfo;
    }

    RxInfo[] rxInfo;

    public LoRaPacket()
    {
    }

    @Id
    @Column(name = "packetId")
    @GeneratedValue
    public int getPacketId()
    {
        return packetId;
    }

    public void setPacketId(int packetId)
    {
        this.packetId = packetId;
    }

    @Basic
    @Column(name = "applicationId")
    public String getApplicationID()
    {
        return applicationID;
    }

    public void setApplicationID(String applicationID)
    {
        this.applicationID = applicationID;
    }

    @Basic
    @Column(name = "applicationName")
    public String getApplicationName()
    {
        return applicationName;
    }

    public void setApplicationName(String applicationName)
    {
        this.applicationName = applicationName;
    }

    @Basic
    @Column(name = "nodeName")
    public String getNodeName()
    {
        return nodeName;
    }

    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }

    @Basic
    @Column(name = "devEUI")
    public String getDevEUI()
    {
        return devEUI;
    }

    public void setDevEUI(String devEUI)
    {
        this.devEUI = devEUI;
    }
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "packet_rxInfo", joinColumns = {
            @JoinColumn(name = "packetId", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "RxInfoId",
                    nullable = false, updatable = false) })
    public Set<RxInfo> getRxInfos()
    {
        return rxInfos;
    }

    public void setRxInfos(Set<RxInfo> rxInfos)
    {
        this.rxInfos = rxInfos;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="txInfo", nullable=false)
    public TxInfo getTxInfo()
    {
        return txInfo;
    }

    public void setTxInfo(TxInfo txInfo)
    {
        this.txInfo = txInfo;
    }

    @Basic
    @Column(name = "fCnt")
    public int getfCnt()
    {
        return fCnt;
    }

    public void setfCnt(int fCnt)
    {
        this.fCnt = fCnt;
    }

    @Basic
    @Column(name = "fPort")
    public int getfPort()
    {
        return fPort;
    }

    public void setfPort(int fPort)
    {
        this.fPort = fPort;
    }

    @Basic
    @Column(name = "data")
    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    @Override
    public void prepare()
    {
        Set<RxInfo> rx = new HashSet<>();
        rx.addAll(Arrays.asList(getRxInfo()));
        setRxInfos(rx);

        getTxInfo().prepare();

        Session s = PersistenceManager.getSession();
        Query query = s.createQuery("from TxInfo");
        List<TxInfo> matches =
                StreamSupport.stream(((Iterable<TxInfo>) query::iterate).spliterator(),false)
                        .filter(d -> d.equals(getTxInfo())).collect(Collectors.toList());
        if(matches.size() > 0) this.setTxInfo(matches.get(0));
        s.close();

        s = PersistenceManager.getSession();
        query = s.createQuery("from RxInfo");
        List<RxInfo> RxInfos =
                StreamSupport.stream(((Iterable<RxInfo>) query::iterate).spliterator(),false)
                        .collect(Collectors.toList());
        s.close();

        RxInfo[] rxs = getRxInfos().toArray(new RxInfo[getRxInfos().size()]);

        /*for(int i = 0; i < rxs.length; i++)
            for(RxInfo r:RxInfos)
                if(rxs[i].equals(r))
                {
                    rxs[i] = r;
                    break;
                }*/

        Set<RxInfo> newRx = new HashSet<>();
        newRx.addAll(Arrays.asList(rxs));
        this.setRxInfos(newRx);
    }

    @Override
    public String toString()
    {
        return "LoRaPacket{" +
                "packetId=" + packetId +
                ", applicationID='" + applicationID + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", devEUI='" + devEUI + '\'' +
                ", rxInfos=" + rxInfos +
                ", txInfo=" + txInfo +
                ", fCnt=" + fCnt +
                ", fPort=" + fPort +
                ", data='" + data + '\'' +
                ", rxInfo=" + Arrays.toString(rxInfo) +
                '}';
    }
}
