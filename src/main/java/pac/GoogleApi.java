package pac;

/**
 * Created by Dmytro Tymoshenko on 13.04.17.
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.gdata.client.spreadsheet.SpreadsheetService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;


public class GoogleApi {
//    @Autowired
//    private GoogleApi googleApi;


    private String sheetsFolderPath;

    private String client_secret;

    /**
     * Application name.
     */
    private final String APPLICATION_NAME = "ModuleSheets Java GoogleApi";

    /**
     * Directory to store user credentials for this application.
     */
    private final java.io.File DATA_STORE_DIR;

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private final JsonFactory JSON_FACTORY;

    /**
     * Global instance of the HTTP transport.
     */
    private HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS,SheetsScopes.DRIVE);

    public GoogleApi(String sheetsFolderPath, String client_secret) {
        this.client_secret = client_secret;
        this.sheetsFolderPath = sheetsFolderPath;
        DATA_STORE_DIR = new java.io.File(
                System.getProperty("user.home"), sheetsFolderPath);
        JSON_FACTORY = JacksonFactory.getDefaultInstance();


        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
//        this.sheetsFolderPath = sheetsFolderPath;
//        this.client_secret = client_secret;
    }

    public SpreadsheetService getSpreadsheetService() throws IOException {

        return new SpreadsheetService(APPLICATION_NAME);
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                GoogleApi.class.getResourceAsStream(client_secret);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     *
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public static void main(String[] args) throws IOException {

        // Build a new authorized API client service.
        GoogleApi googleApi = new GoogleApi(".credentials/sheets.googleapis.com", "/client_secret.json");
        Sheets service = googleApi.getSheetsService();

        // Prints the names and majors of students in a sample spreadsheet:
        // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
        String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        String range = "";  //Class Data!A5:E

//        ValueRange response = service.spreadsheets().values()
//                .get(spreadsheetId, range)
//                .execute();
//
//        List<List<Object>> values = response.getValues();
//        if (values == null || values.size() == 0) {
//            System.out.println("No data found.");
//        } else {
//            System.out.println("Name, Major");
//            for (List row : values) {
//                // Print columns A and E, which correspond to indices 0 and 4.
//                System.out.printf("%s, %s\n", row.get(0) + "______", row.get(4));
//            }
//        }


        Spreadsheet requestBody = new Spreadsheet();

//        Sheets sheetsService = service;
        Sheets.Spreadsheets.Create request = service.spreadsheets().create(requestBody);

        Spreadsheet response1 = request.execute();
//        System.out.println(response1.getSpreadsheetId()+"__________id");
//
//        // TODO: Change code below to process the `response` object:
        System.out.println(response1);

//        Sheet sheet = new Sheet();
//        sheet.setData(new LinkedList<>());


    }

    public String getSheetsFolderPath() {
        return sheetsFolderPath;
    }

    public void setSheetsFolderPath(String sheetsFolderPath) {
        this.sheetsFolderPath = sheetsFolderPath;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getAPPLICATION_NAME() {
        return APPLICATION_NAME;
    }

    public File getDATA_STORE_DIR() {
        return DATA_STORE_DIR;
    }

    public FileDataStoreFactory getDATA_STORE_FACTORY() {
        return DATA_STORE_FACTORY;
    }

    public void setDATA_STORE_FACTORY(FileDataStoreFactory DATA_STORE_FACTORY) {
        this.DATA_STORE_FACTORY = DATA_STORE_FACTORY;
    }

    public JsonFactory getJSON_FACTORY() {
        return JSON_FACTORY;
    }

    public HttpTransport getHTTP_TRANSPORT() {
        return HTTP_TRANSPORT;
    }

    public void setHTTP_TRANSPORT(HttpTransport HTTP_TRANSPORT) {
        this.HTTP_TRANSPORT = HTTP_TRANSPORT;
    }

    public List<String> getSCOPES() {
        return SCOPES;
    }




}



















