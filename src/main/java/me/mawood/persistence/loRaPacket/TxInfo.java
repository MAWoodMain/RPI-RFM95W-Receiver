package me.mawood.persistence.loRaPacket;


import me.mawood.persistence.Persistable;
import me.mawood.persistence.PersistenceManager;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Entity
@Table(name = "tx_info")
public class TxInfo implements Comparable<TxInfo>, Persistable
{
    private int txInfoId;
    private long frequency;
    private DataRate dataRate;
    private boolean adr;
    private String codeRate;


    public TxInfo(long frequency, DataRate dataRate, boolean adr, String codeRate)
    {
        this.frequency = frequency;
        this.dataRate = dataRate;
        this.adr = adr;
        this.codeRate = codeRate;
    }

    public TxInfo()
    {
    }

    @Id
    @Column(name = "txInfoId")
    @GeneratedValue
    public int getTxInfoId()
    {
        return txInfoId;
    }

    public void setTxInfoId(int txInfoId)
    {
        this.txInfoId = txInfoId;
    }

    @Basic
    @Column(name = "frequency")
    public long getFrequency()
    {
        return frequency;
    }

    public void setFrequency(long frequency)
    {
        this.frequency = frequency;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="dataRate", nullable=false)
    public DataRate getDataRate()
    {
        return dataRate;
    }

    public void setDataRate(DataRate dataRate)
    {
        this.dataRate = dataRate;
    }

    @Basic
    @Column(name = "adr")
    public boolean isAdr()
    {
        return adr;
    }

    public void setAdr(boolean adr)
    {
        this.adr = adr;
    }

    @Basic
    @Column(name = "codeRate")
    public String getCodeRate()
    {
        return codeRate;
    }

    public void setCodeRate(String codeRate)
    {
        this.codeRate = codeRate;
    }

    @Override
    public String toString()
    {
        return "TxInfo{" +
                "frequency=" + frequency +
                ", dataRate=" + dataRate +
                ", adr=" + adr +
                ", codeRate='" + codeRate + '\'' +
                '}';
    }

    @Override
    public int compareTo(TxInfo o)
    {
        return Comparator
                .comparing(TxInfo::getDataRate)
                .thenComparing(TxInfo::getCodeRate)
                .thenComparing(TxInfo::getFrequency)
                .thenComparing(TxInfo::isAdr).compare(this, o);
    }

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof TxInfo)) return false;
        return this.compareTo((TxInfo) o) == 0;
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(frequency, dataRate, adr, codeRate);
    }

    @Override
    public void prepare()
    {
        Session s = PersistenceManager.getSession();
        Query query = s.createQuery("from DataRate");
        List<DataRate> matches =  StreamSupport.stream(((Iterable<DataRate>) query::iterate).spliterator(),false).filter(d -> d.equals(getDataRate())).collect(Collectors.toList());
        if(matches.size() > 0) this.setDataRate(matches.get(0));
        s.close();
    }
}
