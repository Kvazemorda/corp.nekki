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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class make parses XML file and send to save it DB
 */
public class Parser {
    private int limitContext = 1024;
    private Logger logger = Logger.getLogger(Parser.class);
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
        XMLStreamReader xmlr = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            xmlr = XMLInputFactory.newInstance().createXMLStreamReader(fis);
            fis.close();
                //start read xml file
                while (xmlr.hasNext()) {
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
                    } else if (xmlr.isStartElement() && xmlr.getLocalName().equals("creationDate")) {
                        xmlr.next();
                        // save date if format is true
                        if (xmlr.hasText() && xmlr.getText().trim().length() > 0) {
                            testEntity.setDateCreated(changeTextToDate(xmlr.getText()));
                        }
                    }

                }
        } catch (XMLStreamException e) {
            logger.error(file.getName() + " " + e);

        } catch (FileNotFoundException e) {
            logger.error(file.getName() + " " + e);
        } catch (IOException e) {
            logger.error(file.getName() + " " + e);
        }

        //save testEntity in DB if field Content and dateCreated not null
        try {
            if (testEntity.getContent() != null && !testEntity.getDateCreated().equals(null)) {
                TestEntityDAO testEntityDAO = new TestEntityDAO();
                testEntityDAO.saveTestEntity(testEntity);
                // move to directory was read
                moveFile(ReaderProperties.dirReadFiles, file.getName());
            } else {
                moveFile(ReaderProperties.dirWrongFiles, file.getName());
                logger.warn(file.getName() + " was moved to wrong directory");
            }
        }catch (NullPointerException e){
            logger.warn(file.getName() + " was moved to wrong directory " + e);
            moveFile(ReaderProperties.dirWrongFiles, file.getName());
            System.out.println(file.getName() + " was moved");
        }
    }

    /**
     * Method move file to another directory
     * @param directoryPath where will move file
     * @param fileName name file you want move
     */
    private void moveFile(String directoryPath, String fileName){
        File fileNew = new File(directoryPath + fileName);
        if(fileNew.exists()){
            fileNew.delete();
        }
        file.renameTo(fileNew);
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
            logger.error(file.getName() + " format date in XML is not correct " + e);
        }
        return date;
    }
}
