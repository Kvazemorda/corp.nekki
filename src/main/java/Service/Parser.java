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
                xmlr.next();
                if (xmlr.isStartElement() && xmlr.getLocalName().equals("content")) {
                    xmlr.next();
                    if (xmlr.hasText() && xmlr.getText().trim().length() > 0 && xmlr.getTextLength() <= limitContext) {
                        testEntity.setContent(xmlr.getText());
                    } else {
                        System.out.println("very much text or very empty :-)");
                    }
                } else if(xmlr.isStartElement() && xmlr.getLocalName().equals("creationDate")){
                    xmlr.next();
                    if(xmlr.hasText() && xmlr.getText().trim().length() > 0){
                        testEntity.setDateCreated(changeTextToDate(xmlr.getText()));
                    }
                }
            }
            File fileRead = new File(ReaderProperties.dirReadFiles + file.getName());
            file.renameTo(fileRead);
            System.out.println(testEntity);
            //save testEntity in DB
            TestEntityDAO testEntityDAO = new TestEntityDAO();
            testEntityDAO.saveTestEntity(testEntity);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param textDate change text to Date format
     * @return
     */
    private Date changeTextToDate(String textDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(textDate);
        } catch (ParseException e) {
            System.out.println("Not correct date format in XML file ");
            e.printStackTrace();
        }
        return date;
    }
}
