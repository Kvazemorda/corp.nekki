package Service;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Клас перебирает все xml файлы в дериктории
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
                System.out.println("В директории нет файлов");
            }else {
                getFilesToDirectory();
            }
        }
    }

    /**
     * Перебираем все файлы директории и отправляем их на парсинг
     */
    public void getFilesToDirectory(){
        ExecutorService service = Executors.newFixedThreadPool(10);
        for(File file: files){
            if(file.isFile() && isXMLFile(file)){
                continue;
            }
            service.execute(new Runnable() {
                @Override
                public void run() {
                    Parser parser = new Parser();
                }
            });
        }
    }

    /**
     * Проверяю расширение файла
     * @param file
     * @return
     */
    private boolean isXMLFile(File file){
        //Проверяю есть ли точка в имени фала, если точка не первый символ и расширение файла XML то это
        //XML файл.
        if(file.getName().lastIndexOf(".") != -1 && file.getName().lastIndexOf(".") != 0 &&
                file.getName().substring(file.getName().indexOf(".") + 1).equals("xml")){
            return true;
        }else {
            return false;
        }
    }

}
