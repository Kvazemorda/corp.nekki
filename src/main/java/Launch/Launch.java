package Launch;

import Hibernate.HibernateSessionFactory;
import Service.FilesXML;
import org.hibernate.Session;

public class Launch {
    public static Session session;

    public static void main(String[] args) {
        session = HibernateSessionFactory.getSessionFactory().openSession();
        String pathDirectory = "C:\\Users\\admin\\Documents\\GitHub\\corp.nekki\\filesForRead";
        FilesXML filesXML = new FilesXML(pathDirectory);
        session.close();
        HibernateSessionFactory.shutdown();
    }
}
