package Hibernate;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Class create connect to DB
 */
public class HibernateSessionFactory {
    private static final Logger logger = Logger.getLogger(HibernateSessionFactory.class);
    private static SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Create session
     * @return
     */
    protected static SessionFactory buildSessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            logger.error(e);
            throw new ExceptionInInitializerError("Does not have connected to data base. Check user name, " +
                    "password and path to data base in hibernate.cfg.xml ");
        }

        return sessionFactory;
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    /**
     * Close session
     */
    public static void shutdown(){
        getSessionFactory().close();
    }

}
