package Service;

import DAO.TestEntityDAO;
import Entity.TestEntity;
import org.apache.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class make parses XML file and send to save it DB
 */
public class Parser {
    private int limitContext = 1024;
    private static final Logger logger = Logger.getLogger(Parser.class);
    private File file;

    public Parser(File file) {
        this.file = file;
    }

    /**
     * method get XML File, read and check this file on correct format.
     * if all correct he save it data base and move file to another directory.
     * if file wrong file it moves to directory with wrong files     */
    public void startParsXML(){
        TestEntity testEntity = new TestEntity();
        try {
            XMLStreamReader xmlr = null;
            try {
                xmlr = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                logger.error(e);
                e.printStackTrace();
            }
            //start read xml file
            while (xmlr.hasNext()){
                xmlr.next();
                //if file has start element and tag equals - content then read next row
                if (xmlr.isStartElement() && xmlr.getLocalName().toLowerCase().equals("content")) {
                    xmlr.next();
                    //save text from tag "content" if text more then 0 or less then 1024
                    if (xmlr.hasText() && xmlr.getText().trim().length() > 0 && xmlr.getTextLength() <= limitContext) {
                        testEntity.setContent(xmlr.getText());
                    } else {
                        logger.error(file.getName() + " tag content - count simbols less then 0 or more then 1024 ");
                        //move wrong file to directory with other wrong files
                        }
                    //read next row if tag name equals "creationDate"
                    } else if(xmlr.isStartElement() && xmlr.getLocalName().equals("creationDate")){
                    xmlr.next();
                    // save date if format is true
                    if(xmlr.hasText() && xmlr.getText().trim().length() > 0){
                        testEntity.setDateCreated(changeTextToDate(xmlr.getText()));
                    }
                }
            }

            //save testEntity in DB if field Content and dateCreated not null
            if(testEntity.getContent() != null && !testEntity.getDateCreated().equals(null)){
                TestEntityDAO testEntityDAO = new TestEntityDAO();
                testEntityDAO.saveTestEntity(testEntity);
                // move to directory was read
                File fileRead = new File(ReaderProperties.dirReadFiles + file.getName());
                file.renameTo(fileRead);
            }else{
                File fileWrong = new File(ReaderProperties.dirWrongFiles + file.getName());
                file.renameTo(fileWrong);
                logger.warn(file.getName() + " was moved to wrong directory");
            }

        } catch (XMLStreamException e) {
            e.printStackTrace();
            logger.error(e);
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
            logger.error(file.getName() + " format date in XML is not correct");
            e.printStackTrace();
        }
        return date;
    }
}
