package pac;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by Dmytro Tymoshenko on 12.04.17.
 */
@Repository
public class ModuleFIleImpl implements ModuleFile {

    @Autowired
    @Qualifier("getGoogleApi")
    private GoogleApi googleApi;

    private Map<String, String> titlesMap = new HashMap<>();

//    private List<SpreadsheetEntry> spreadsheetEntryList;

    private Sheets service ;

    private SpreadsheetService spreadsheetService ;

    @PostConstruct
    public void initALlSpreadsheets() throws IOException, ServiceException {
        service = googleApi.getSheetsService();
        spreadsheetService = googleApi.getSpreadsheetService();
//        URL SPREADSHEET_FEED_URL = null;
//        try {
//            SPREADSHEET_FEED_URL = new URL(
//                    "https://spreadsheets.google.com/feeds/spreadsheets/private/full");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//
//        // Make a request to the API and get all spreadsheets.
//        SpreadsheetFeed feed = spreadsheetService.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
//        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
//
//
//
//        spreadsheets.forEach((e)->{
//            titlesMap.put(e.getTitle().getPlainText(), e.getId());
//            System.out.println(e.getTitle().getPlainText()+"   "+ e.getId());
//
//        });
//        Sheets.Spreadsheets spreadsheets = service.spreadsheets();



    }

    public ModuleFIleImpl() throws IOException {
    }


    @Override
    public Spreadsheet createFile(String fileName) throws IOException {
        if (!titlesMap.containsKey(fileName)) {
            Spreadsheet requestBody = new Spreadsheet();
            Properties properties = new Properties();
            properties.setProperty("title", fileName);
            Sheets.Spreadsheets.Create request = service.spreadsheets().create(requestBody);

            Spreadsheet response1 = request.execute();
            titlesMap.put(fileName, response1.getSpreadsheetId());
            return response1;
        }

        return null;


    }

    @Override
    public List<Spreadsheet> getAllFiles() {
        List<Spreadsheet> list = new ArrayList<>();
        titlesMap.entrySet().forEach((e)->{

            try {
                list.add(service.spreadsheets().get(e.getValue()).execute());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        return list;
    }

    @Override
    public List<Sheet> getListByFileName(String name) {

        return null;
    }

    @Override
    public List<List<Object>> extractContent(String fileName) {

        if (titlesMap.containsKey(fileName)) {
            BatchGetValuesResponse response = null;
            try {
                response = service.spreadsheets().values()
                        .batchGet(titlesMap.get(fileName))
                        .execute();

                List<ValueRange> valueRanges = response.getValueRanges();
                List<List<Object>> sheetsFromSpread = new ArrayList<>();

                valueRanges.forEach((e)->{
                    sheetsFromSpread.addAll(e.getValues());
                });
                return sheetsFromSpread;
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    @Override
    public List<List<Object>> extractSheetContent(String fileName, String sheetName) {
        if (titlesMap.containsKey(fileName)) {

            try {

                String range = sheetName;
                ValueRange response = service.spreadsheets().values()
                        .get(titlesMap.get(fileName), range)
                        .execute();

                if (response != null) {
                    List<List<Object>> values = response.getValues();
                    return values;
                }else return null;
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }


}

//class MySpreadsheetIntegration {
//    public static void main(String[] args)
//            throws AuthenticationException, MalformedURLException, IOException, ServiceException {
//
//        SpreadsheetService service =
//                new SpreadsheetService("MySpreadsheetIntegration-v1");
//
//        // TOD//O: Authorize the service object for a specific user (see other sections)
//
//        // Define the URL to request.  This should never change.
//        URL SPREADSHEET_FEED_URL = new URL(
//                "https://spreadsheets.google.com/feeds/spreadsheets/private/full");
//
//        // Make a request to the API and get all spreadsheets.
//        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
//        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
//
//        // Iterate through all of the spreadsheets returned
//        for (SpreadsheetEntry spreadsheet : spreadsheets) {
//            // Print the title of this spreadsheet to the screen
//            System.out.println(spreadsheet.getTitle().getPlainText());
//        }
//    }
//}
//    private final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";

//    public void createFile(String fileName, int rows, int columns) {
//
//        String content ="just now";
//        List<Object> data1 = new ArrayList<>();
//        data1.add (content);
//        List<List<Object>> data = new ArrayList<>();
//        data.add (data1);
//        ValueRange valueRange=new ValueRange();
//        valueRange.setValues(data);
//        Sheets service = null;
////        try {
////            service = googleApi.getSheetsService();
////            service.spreadsheets().values().
////                    append(spreadsheetId, range, valueRange)
////                    .setValueInputOption("RAW")
////                    .execute();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//    }