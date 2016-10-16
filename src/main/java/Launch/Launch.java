package Launch;

import Service.FilesXML;
import Service.ReaderProperties;

public class Launch {

    public static void main(String[] args) {
        ReaderProperties readerProperties = new ReaderProperties();
        FilesXML filesXML = new FilesXML(ReaderProperties.dirUnReadFiles);
    }
}
