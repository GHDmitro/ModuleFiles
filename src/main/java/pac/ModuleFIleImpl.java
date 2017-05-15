package pac;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by Dmytro Tymoshenko on 12.04.17.
 */
@Service
public class ModuleFIleImpl implements ModuleFile {

    @Autowired
    private GoogleApi googleApi;

    private Map<String, String> titlesMap = new HashMap<>();

//    private List<SpreadsheetEntry> spreadsheetEntryList;

    private Sheets service = googleApi.getSheetsService();

    private SpreadsheetService spreadsheetService = googleApi.getSpreadsheetService();

    @PostConstruct
    public void initALlSpreadsheets() throws IOException, ServiceException {
        URL SPREADSHEET_FEED_URL = null;
        try {
            SPREADSHEET_FEED_URL = new URL(
                    "https://spreadsheets.google.com/feeds/spreadsheets/private/full");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Make a request to the API and get all spreadsheets.
        SpreadsheetFeed feed = spreadsheetService.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();



        spreadsheets.forEach((e)->{
            titlesMap.put(e.getTitle().getPlainText(), e.getId());
            System.out.println(e.getTitle().getPlainText()+"   "+ e.getId());

        });
//        Sheets.Spreadsheets spreadsheets = service.spreadsheets();



    }

    public ModuleFIleImpl() throws IOException {
    }


    @Override
    public void createFile(String fileName) throws IOException {
        if (!titlesMap.containsKey(fileName)) {
            Spreadsheet requestBody = new Spreadsheet();
            Properties properties = new Properties();
            properties.setProperty("title", fileName);
            Sheets.Spreadsheets.Create request = service.spreadsheets().create(requestBody);

            Spreadsheet response1 = request.execute();
            titlesMap.put(fileName, response1.getSpreadsheetId());
        }

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

//                sheetsFromSpread.forEach((e)-> System.out.println(e.get(0)));
//                List<List<Object>>values = new List<List<Object>>;


//        if (values == null || values.size() == 0) {
//            System.out.println("No data found.");
//        } else {
//            System.out.println("Name, Major");
//            for (List row : values) {
//                // Print columns A and E, which correspond to indices 0 and 4.
//                System.out.printf("%s, %s\n", row.get(0) + "______", row.get(4));
//            }
//        }
                return sheetsFromSpread;
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
//        // TODO: Authorize the service object for a specific user (see other sections)
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