package Service;

import DAO.TestEntityDAO;
import Entity.TestEntity;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parser {
    int limitContext = 1024;
    FileInputStream fis;

    public Parser() {
    }

    public void startParsXML(File file){
        TestEntity testEntity = new TestEntity();
        try {
            XMLStreamReader xmlr = null;
            try {
                xmlr = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (xmlr.hasNext()){
                //if tag is "content" - save into TestEntity
                if(xmlr.getLocalName().equals("content")){
                    if(xmlr.getTextLength() <= limitContext){
                        testEntity.setContent(xmlr.getText());
                        //if tag is "creationDate" - save into TestEntity
                    }else if(xmlr.getLocalName().equals("creationDate")){
                        testEntity.setDateCreated(changeTextToDate(xmlr.getText()));
                    }else {
                        System.out.println("Instert log and transer file to wrong directory");
                    }
                }
            }
            //save testEntity in DB
            TestEntityDAO testEntityDAO = new TestEntityDAO();
            testEntityDAO.saveTestEntity(testEntity);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод переводит текст в формат даты
     * @param textDate
     * @return
     */
    private Date changeTextToDate(String textDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(textDate);
        } catch (ParseException e) {
            System.out.println("Не верный формат даты в XML файле");
            e.printStackTrace();
        }
        return date;
    }
}
