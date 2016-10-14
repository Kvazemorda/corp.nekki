package DAO;

import Entity.TestEntity;
import Launch.Launch;

/**
 * Клас служит для сохранения, удаления, получения данных БД
 */
public class TestEntityDAO {

    /**
     * Класс получает собранные данные из xml файла и сохраняет их в БД
     * @param testEntity
     */
    public void saveTestEntity(TestEntity testEntity){
        Launch.session.beginTransaction();
        Launch.session.save(testEntity);
        Launch.session.getTransaction().commit();
    }
}
