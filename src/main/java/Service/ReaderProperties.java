package Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReaderProperties {
    public static String dirUnReadFiles, dirReadFiles, dirWrongFiles;
    public ReaderProperties() {
        InputStream inputStream;
        Properties properties = new Properties();

        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            dirUnReadFiles = properties.getProperty("dirUnReadFiles");
            dirReadFiles = properties.getProperty("dirReadFiles");
            dirWrongFiles = properties.getProperty("dirWrongFiles");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
