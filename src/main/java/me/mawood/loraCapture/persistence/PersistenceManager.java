package me.mawood.loraCapture.persistence;

import me.mawood.loraCapture.persistence.loRaPacket.DataRate;
import me.mawood.loraCapture.persistence.loRaPacket.LoRaPacket;
import me.mawood.loraCapture.persistence.loRaPacket.RxInfo;
import me.mawood.loraCapture.persistence.loRaPacket.TxInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.Collections;

public class PersistenceManager
{
    private static final SessionFactory sessionFactory;
    static
    {
        Configuration configuration = new Configuration();

        // Register classes used
        configuration.addAnnotatedClass(me.mawood.loraCapture.persistence.loRaPacket.DataRate.class);

        configuration.configure();
        ServiceRegistryBuilder registry = new ServiceRegistryBuilder();
        registry.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = registry.buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static Session getSession()
    {
        return sessionFactory.openSession();
    }

    public void store(Persistable o) {
        o.prepare();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(o);
        session.getTransaction().commit();
        session.close();
    }

    public void exit() {
        sessionFactory.close();
    }

    public static void main(String[] args)
    {
        PersistenceManager pm = new PersistenceManager();

        DataRate dr = new DataRate();
        dr.setBandwidth(125);
        dr.setModulation("LORA");
        dr.setSpreadFactor(9);
        TxInfo tx = new TxInfo();
        tx.setDataRate(dr);
        tx.setAdr(true);
        tx.setCodeRate("4/5");
        tx.setFrequency(868200000);

        RxInfo rxInfo = new RxInfo();
        rxInfo.setAltitude(1);
        rxInfo.setLatitude(1.2);
        rxInfo.setLongitude(2.3);
        rxInfo.setLoRaSNR(2);
        rxInfo.setMac("abc");
        rxInfo.setName("test");
        rxInfo.setRssi(-115);
        rxInfo.setTime("11:52:11");

        LoRaPacket packet = new LoRaPacket();
        packet.setTxInfo(tx);
        packet.setRxInfos(Collections.singleton(rxInfo));
        packet.setApplicationID("1");
        packet.setData("hello");
        packet.setApplicationName("test");
        packet.setDevEUI("abcd");
        packet.setfCnt(1);
        packet.setfPort(1);
        packet.setNodeName("test_node");


        pm.store(packet);
    }
}
