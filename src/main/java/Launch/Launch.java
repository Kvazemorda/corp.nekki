package Launch;

import Hibernate.HibernateSessionFactory;
import Service.FilesXML;
import Service.ReaderProperties;
import org.hibernate.Session;

public class Launch {

    public static void main(String[] args) {
        ReaderProperties readerProperties = new ReaderProperties();
        // Launch if config.property is correct
        while (true){
            if(!readerProperties.isDirReadFilesIsUnexpected() && !readerProperties.isDirUnReadFilesIsUnexpected()
                    && !readerProperties.isDirWrongReadFilesIsUnexpected() && !readerProperties.isDirEquals()){
                Session session = HibernateSessionFactory.getSessionFactory().openSession();

                FilesXML filesXML = new FilesXML(ReaderProperties.dirUnReadFiles);
                try {
                    Thread.sleep((long) ReaderProperties.monitorPeriod);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
