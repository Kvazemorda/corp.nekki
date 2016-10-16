package Launch;

import Service.FilesXML;
import Service.ReaderProperties;

public class Launch {

    public static void main(String[] args) {

        ReaderProperties readerProperties = new ReaderProperties();
        // Launch if config.property is correct
        while (true){
            if(!readerProperties.isDirReadFilesIsUnexpected() && !readerProperties.isDirUnReadFilesIsUnexpected()
                    && !readerProperties.isDirWrongReadFilesIsUnexpected()){
                FilesXML filesXML = new FilesXML(ReaderProperties.dirUnReadFiles);
                System.out.println("sleep on minutes");
                try {
                    Thread.sleep(ReaderProperties.monitorPeriod);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
