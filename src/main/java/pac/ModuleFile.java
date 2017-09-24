package pac;

import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Dmytro Tymoshenko on 12.04.17.
 */
public interface ModuleFile {

    Spreadsheet createFile(String fileName) throws IOException;

    List<Spreadsheet> getAllFiles() throws MalformedURLException;

    List<Sheet> getListByFileName(String name);

    List<List<Object>> extractContent(String fileName);

    List<List<Object>> extractSheetContent(String fileName, String sheetName);
}
