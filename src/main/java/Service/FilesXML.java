package Service;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class check all xml files in directory
 */
public class FilesXML {
    private String path;
    private File[] files;

    public FilesXML(String path) {
        this.path = path;
        File catalogFiles = new File(path);
        //проверяю что директория существует и она является директорией
        if(catalogFiles.exists() || catalogFiles.isDirectory()){
            files = catalogFiles.listFiles();
            if(files == null){
                System.out.println("Directory don't have files");
            }else {
                getFilesToDirectory();
            }
        }
    }

    /**
     * Check all files in directory and send to parser
     */
    public void getFilesToDirectory(){
        //Create 10 thread Pool
        ExecutorService service = Executors.newFixedThreadPool(10);
        for(File file: files){
            // file is file and is XML fle
            if(file.isFile() && isXMLFile(file)){
                continue;
            }
            service.execute(new Runnable() {
                @Override
                public void run() {
                    Parser parser = new Parser();
                    parser.startParsXML(file);
                }
            });
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
        // Проверяю есть ли точка в имени фала, если точка не первый символ и расширение файла XML то это
        //XML файл.
        if(file.getName().lastIndexOf(".") != -1 && file.getName().lastIndexOf(".") != 0 &&
                file.getName().substring(file.getName().indexOf(".") + 1).equals("xml")){
            return true;
        }else {
            return false;
        }
    }

}
