package Hibernate;

import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;


import javax.persistence.Query;

public class CheckTable {

    public CheckTable() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
    try{
        Query createQuery = session.createQuery("select content from TestEntity");
    }catch(SQLGrammarException e){
        Query createTable = session.createSQLQuery("CREATE TABLE test\n" +
                "(\n" +
                "  content CHAR(1024) NOT NULL,\n" +
                "  datecreated TIMESTAMP NOT NULL,\n" +
                "  id SERIAL PRIMARY KEY NOT NULL\n" +
                ");");
        session.getTransaction().commit();
        session.close();
    }

    }
}
