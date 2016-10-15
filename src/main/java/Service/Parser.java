package Service;

import Entity.TestEntity;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Parser {
    int limitContext = 1024;
    FileInputStream fis;

    public Parser() {
    }

    public void startParsXML(String fileName){
        TestEntity testEntity = new TestEntity();
        try {
            XMLStreamReader xmlr = null;
            try {
                xmlr = XMLInputFactory.newInstance().createXMLStreamReader(fileName, new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (xmlr.hasNext()){
                if(xmlr.getLocalName().equals("content")){
                    if(xmlr.getTextLength() <= limitContext){
                        testEntity.setContent(xmlr.getText());
                    }else{
                        System.out.println("здесь должен быть лог и перенос файла в ошибочные");
                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
