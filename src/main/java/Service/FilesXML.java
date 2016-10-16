package Service;

import Hibernate.HibernateSessionFactory;

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
        //directory is directory or it exists?
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
            }
        }

        service.shutdown();

        while (true){
            if(!service.isShutdown()){
                HibernateSessionFactory.shutdown();
            }else{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


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
