package Service;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class check all xml files in directory
 */
public class FilesXML {
    private String path;
    private File[] files;
    private static Logger logger = Logger.getLogger(FilesXML.class);

    public FilesXML(String path) {
        this.path = path;
        File catalogFiles = new File(path);
        //directory is directory or it exists?
        if(catalogFiles.exists() || catalogFiles.isDirectory()){
            files = catalogFiles.listFiles();
            if(files.length == 0){
                logger.info("В директории нет файлов для чтения");
            }else {
                getFilesToDirectory();
            }
        }
    }

    /**
     * Check all files in directory and send to parser
     */
    public final void getFilesToDirectory(){
        //Create 10 thread Pool
        ExecutorService service = Executors.newFixedThreadPool(10);

        for(final File file: files){
            // file is file and is XML file
            if(file.isFile() && isXMLFile(file)) {
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        Parser parser = new Parser();
                        parser.startParsXML(file);
                    }
                });
            }else{
                File fileWrong = new File(ReaderProperties.dirWrongFiles + file.getName());
                file.renameTo(fileWrong);
                logger.info("В папке для чтения нет XML файлов");
            }
        }
        service.shutdown();
    }

    /**
     * Check file - must be XML extention
     * @param file
     * @return
     */
    private boolean isXMLFile(File file){
        // If has dit, name not start from dot and file extention has XML
        if(file.getName().lastIndexOf(".") != -1 && file.getName().lastIndexOf(".") != 0 &&
                file.getName().substring(file.getName().indexOf(".") + 1).equals("xml")){
            return true;
        }else {
            return false;
        }
    }

}
