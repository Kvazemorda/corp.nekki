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
import java.util.NoSuchElementException;

/**
 * Class make parses XML file and send to save it DB
 */
public class Parser {
    private int limitContext = 1024;
    private Logger logger = Logger.getLogger(Parser.class);
    private File file;
    private FileInputStream fis = null;
    private TestEntity testEntity;
    private XMLStreamReader xmlr = null;

    public Parser(File file) {
        this.file = file;
    }

    /**
     * method get XML File, read and check this file on correct format.
     * if all correct he save it data base and move file to another directory.
     * if file wrong file it moves to directory with wrong files     */
    public void startParsXML(){
        testEntity = new TestEntity();
        try {
            fis = new FileInputStream(file);
            xmlr = XMLInputFactory.newInstance().createXMLStreamReader(fis);
            boolean entryStart = false;
            boolean entryEnd = false;
            Date dateCreation = null;
            String textContent = null;
            String textDateCreation = null;
            //start read xml file
                while (xmlr.hasNext()) {
                    xmlr.next();
                    //Tag enrty start
                    if (xmlr.isStartElement() && xmlr.getLocalName().toLowerCase().equals("entry")) {
                        xmlr.next();
                        entryStart = true;
                    }

                    if(textContent == null){
                        textContent = getTextFromTag("content");
                    }
                    if(textDateCreation == null){
                        textDateCreation = getTextFromTag("creationdate");
                    }
                    if (textDateCreation != null){
                        dateCreation = changeTextToDate(textDateCreation);
                    }
                    if (xmlr.isEndElement() && xmlr.getLocalName().toLowerCase().equals("entry")){
                        entryEnd = true;
                    }
                    if (entryStart && entryEnd && textContent != null && textContent.length() <= limitContext && dateCreation != null){
                            testEntity.setContent(textContent);
                            testEntity.setDateCreated(dateCreation);
                        } else {
                            logger.error(file.getName() + " XML is not correct format");
                        }
                    }
        } catch (XMLStreamException e) {
            logger.error(file.getName() + " " + e);
        } catch (FileNotFoundException e) {
            logger.error(file.getName() + " " + e);
        } catch (NoSuchElementException e){
            logger.error(file.getName() + " " + e);
        }

        saveInfoToDB();
    }

    /**
     * Method return text from tag
     * @param tag
     * @return
     */
    private String getTextFromTag(String tag) throws XMLStreamException, NoSuchElementException {
        String textFromTag = null;
            //read next row if equals tag
            if (xmlr.isStartElement() && xmlr.getLocalName().toLowerCase().equals(tag)){
                xmlr.next();
                //save text to temporary variable from tag "content" if text more then 0 or less then 1024
                if (xmlr.hasText() && xmlr.getText().trim().length() > 0 && xmlr.getTextLength() <= limitContext) {
                    String textTemp = xmlr.getText();
                    xmlr.next();
                    //save text to textEntity if tag has end
                    if (xmlr.isEndElement() && xmlr.getLocalName().toLowerCase().equals(tag)) {
                        textFromTag = textTemp;
                    }
                }
            }
        return textFromTag;
    }

    /**
     * save testEntity in DB if field Content and dateCreated not null
     */
    private void saveInfoToDB(){
        try {
            fis.close();
            if (testEntity.getContent() != null && testEntity.getDateCreated() != null) {
                TestEntityDAO testEntityDAO = new TestEntityDAO();
                testEntityDAO.saveTestEntity(testEntity);
                // move to directory was read
                moveFile(ReaderProperties.dirReadFiles, file.getName());
            } else {
                moveFile(ReaderProperties.dirWrongFiles, file.getName());
                logger.warn(file.getName() + " was moved to wrong directory");
            }
        }catch (NullPointerException e){
            logger.error(file.getName() + " " + e);
        } catch (IOException e) {
            logger.error(file.getName() + " " + e);
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
