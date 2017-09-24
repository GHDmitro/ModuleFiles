package pactest;

import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import pac.ModuleConfig;
import pac.ModuleFile;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Dmytro Tymoshenko on 15.05.17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ModuleConfig.class,loader=AnnotationConfigContextLoader.class)
public class TestClass {


    @Autowired
//    @Qualifier(value = "getModuleFile")
    private ModuleFile moduleFile;
//    @Autowired
//    private GoogleApi googleApi;

    @Before
    public void init() throws IOException {

    }

    @Test
    public void createFileTest() throws IOException {

        String nameFile = "myFirst";

        Spreadsheet file = moduleFile.createFile(nameFile);

        assertNotNull(file);
        assertNotNull(file.getSpreadsheetId());
        assertNotNull(file.getSheets());


    }

    @Test
    public void getListByFileNameTest(){
        String nameFile = "myFirst";

        List<Sheet> sheetList = moduleFile.getListByFileName(nameFile);

        assertNotNull(sheetList);
        assertTrue(sheetList.size() > 0);



    }
    @Test
    public void getAllSheetsTest(){



    }

}















