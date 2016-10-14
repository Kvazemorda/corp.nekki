import org.junit.Test;

import java.text.SimpleDateFormat;

public class TestParsing {

    public TestParsing() {
    }

    @Test
    public void parsingIsCorrect(){
        String fTaskContent = "Содержимое записи 1";
        String fTask2Content = "Содержимое записи 2";
        String fTask3Content = "Содержимое записи 3";
        String fTask4Content = "Содержимое записи 4";

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH-mm-ss");
        String fTaskDate = "2014-01-01 00:00:00";
        String fTask2Date = "2014-02-04 00:00:00";
        String fTask3Date = "2015-02-04 00:00:00";
        String fTask4Date = "2015-10-04 00:00:00";

    }

}
