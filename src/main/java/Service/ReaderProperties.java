package Service;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class for read config.property and check on correct attribute
 */
public class ReaderProperties {
    public static String dirUnReadFiles, dirReadFiles, dirWrongFiles;
    public static int monitorPeriod;
    private boolean dirUnReadFilesIsUnexpected, dirReadFilesIsUnexpected, dirWrongReadFilesIsUnexpected,
            dirEquals;
    private static Logger logger = Logger.getLogger(ReaderProperties.class);

    public ReaderProperties() {
        InputStream inputStream;
        Properties properties = new Properties();

        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);

            dirUnReadFiles = properties.getProperty("dirUnReadFiles");
            dirReadFiles = properties.getProperty("dirReadFiles");
            dirWrongFiles = properties.getProperty("dirWrongFiles");
            String monitorPeriodStr = properties.getProperty("monitorPeriod");
            monitorPeriod = Integer.valueOf(monitorPeriodStr) * 1000 * 60;


            File unReadFiles = new File(dirUnReadFiles);
            //unReadFiles is directory or it exists?
            if(!unReadFiles.exists() || !unReadFiles.isDirectory()) {
                dirUnReadFilesIsUnexpected = true;
                logger.error("Not correct directory path in config.property for new files for reader "
                        + dirUnReadFiles);
                System.out.println("Not correct directory path in config.property for new files for reader "
                        + dirUnReadFiles);
            }
            File readFiles = new File(dirReadFiles);
            //readFiles is directory or it exists?
            if(!readFiles.exists() || !readFiles.isDirectory()) {
                dirReadFilesIsUnexpected = true;
                logger.error("Not correct directory path in config.property for was read files " + dirReadFiles);
                System.out.println("Not correct directory path in config.property for was read files" + dirReadFiles);
            }
            File wrongFiles = new File(dirWrongFiles);
            //wrongFiles is directory or it exists?
            if(!wrongFiles.exists() || !wrongFiles.isDirectory()) {
                dirWrongReadFilesIsUnexpected = true;
                logger.error("Not correct directory path in config.property for wrong files" + dirWrongFiles);
                System.out.println("Not correct directory path in config.property for wrong files" + dirWrongFiles );
            }
            if(dirReadFiles.equals(dirUnReadFiles)){
                dirEquals = true;
            }
            if(dirReadFiles.equals(dirWrongFiles)){
                dirEquals = true;
            }
            if(dirWrongFiles.equals(dirUnReadFiles)){
                dirEquals = true;
            }


        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } catch (NumberFormatException e){
            logger.error("Monitor period must be number " + e);
            System.out.println("Monitor period must be number");
        }
    }
    public boolean isDirUnReadFilesIsUnexpected() {
        return dirUnReadFilesIsUnexpected;
    }

    public boolean isDirReadFilesIsUnexpected() {
        return dirReadFilesIsUnexpected;
    }

    public boolean isDirWrongReadFilesIsUnexpected() {
        return dirWrongReadFilesIsUnexpected;
    }

    public boolean isDirEquals() {
        return dirEquals;
    }
}
