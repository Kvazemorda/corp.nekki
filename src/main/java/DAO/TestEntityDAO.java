package DAO;

import Entity.TestEntity;
import Launch.Launch;

/**
 * Class saved, delete and get entity from DB
 */
public class TestEntityDAO {

    /**
     * @param testEntity save in DB
     */
    public void saveTestEntity(TestEntity testEntity){
        Launch.session.beginTransaction();
        Launch.session.save(testEntity);
        Launch.session.getTransaction().commit();
    }
}
