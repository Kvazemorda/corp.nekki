package DAO;

import Entity.TestEntity;
import Hibernate.HibernateSessionFactory;
import org.hibernate.Session;

/**
 * Class saved, delete and get entity from DB
 */
public class TestEntityDAO {
    private Session session;

    public TestEntityDAO() {
        session = HibernateSessionFactory.getSessionFactory().openSession();
    }

    /**
     * @param testEntity save in DB
     */
    public void saveTestEntity(TestEntity testEntity){
        session.beginTransaction();
        session.save(testEntity);
        session.getTransaction().commit();
        session.close();

    }
}
