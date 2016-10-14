package Launch;

import Hibernate.HibernateSessionFactory;
import org.hibernate.Session;

public class Launch {
    public static Session session;

    public static void main(String[] args) {
        session = HibernateSessionFactory.getSessionFactory().openSession();

        session.close();
        HibernateSessionFactory.shutdown();
    }
}
