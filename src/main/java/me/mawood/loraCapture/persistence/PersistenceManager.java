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
}
