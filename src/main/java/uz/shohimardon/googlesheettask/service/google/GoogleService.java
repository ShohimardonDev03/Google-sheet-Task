package uz.shohimardon.googlesheettask.service.google;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import uz.shohimardon.googlesheettask.utils.Db;


import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static uz.shohimardon.googlesheettask.utils.Db.grid;
import static uz.shohimardon.googlesheettask.utils.Db.sheetId;

@Service
@RequiredArgsConstructor
public class GoogleService {
    public static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    private final ApplicationEventPublisher applicationEventPublisher;
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS);


    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File("StoredCredential")))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";

    public static Sheets getSheets() {
        final NetHttpTransport HTTP_TRANSPORT = getHttp_transport();

        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getHttpRequestInitializer(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        return service;
    }

    private static Credential getHttpRequestInitializer(NetHttpTransport HTTP_TRANSPORT) {
        try {
            return getCredentials(HTTP_TRANSPORT);
        } catch (IOException e) {
            e.printStackTrace();
            return getHttpRequestInitializer(HTTP_TRANSPORT);
        }
    }

    private static NetHttpTransport getHttp_transport() {

        NetHttpTransport HTTP_TRANSPORT = null;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {

            System.out.println("````````````````````````````````````````````````````");
            System.out.println("NetHttpTransport");
            System.out.println("````````````````````````````````````````````````````");

            System.out.println("e = " + e);
            GeneralSecurityException generalSecurityException = new GeneralSecurityException(e);

            generalSecurityException.printStackTrace();
//            return getHttp_transport();
        }

        return HTTP_TRANSPORT;
    }


    public static Request size(DimensionRange dimensionRange, int size) {
        return new Request().setUpdateDimensionProperties(
                new UpdateDimensionPropertiesRequest()
                        .setRange(
                                dimensionRange
                        )
                        .setProperties(
                                new DimensionProperties()
                                        .setPixelSize(size)

                        )

                        .setFields("pixelSize")

        );
    }

    public static DimensionRange getRange(Integer sheetID, Integer endIndex, int startIndex, String dimension) {
        return new DimensionRange()
                .setSheetId(sheetID)
                .setDimension(dimension)
                .setStartIndex(startIndex)
                .setEndIndex(endIndex);
    }

    public DimensionRange getRange(Integer sheetID, int startIndex, String dimension) {
        return new DimensionRange()
                .setSheetId(sheetID)
                .setDimension(dimension)
                .setStartIndex(startIndex);
    }


    public static Request updateColumnStyle(GridRange gridRange) {
        return updateColumnStyle(gridRange, true);
    }


    public static Request updateColumnStyle(GridRange gridRange, boolean isBolte) {
        Border buttom = new Border();

        String solid_thick = "SOLID";
        buttom.setStyle(solid_thick);


        return new Request().setRepeatCell(new RepeatCellRequest()

                .setCell(
                        new CellData()

                                .setUserEnteredFormat(

                                        new CellFormat()


                                                .setTextFormat(
                                                        new TextFormat().setBold(isBolte)


                                                )

                                                .setBorders(new Borders()
                                                        .setBottom(buttom)
                                                        .setLeft(buttom)
                                                        .setTop(buttom)
                                                        .setTop(buttom)
                                                )


                                                .setHorizontalAlignment("CENTER")
                                )


                )
                .setFields("*")

                .setRange(gridRange)
        );


    }

    public static Request updateColumnStyle(GridRange gridRange, float red, float green, float blue) {

        return updateColumnStyle(gridRange, red, green, blue, 0, 0, 0);
    }

    public static Request updateColumnStyle(GridRange gridRange, float red, float green, float blue, float textRed, float textGreen, float textBlue) {
        Border bottom = new Border();

        String solid_thick = "SOLID";
        bottom.setStyle(solid_thick);


        return new Request().setRepeatCell(new RepeatCellRequest()

                .setCell(
                        new CellData()

                                .setUserEnteredFormat(

                                        new CellFormat()


                                                .setTextFormat(
                                                        new TextFormat()
                                                                .setBold(true)
                                                                .setForegroundColor(new Color()
                                                                        .setRed(textRed)
                                                                        .setGreen(textGreen)
                                                                        .setBlue(textBlue)

                                                                )


                                                )

                                                .setBackgroundColor(
                                                        new Color()
                                                                .setBlue(blue)
                                                                .setRed(red)
                                                                .setGreen(green)
                                                )
                                                .setBorders(new Borders()
                                                        .setBottom(bottom)
                                                        .setLeft(bottom)
                                                        .setTop(bottom)
                                                        .setRight(bottom)
                                                )


                                                .setHorizontalAlignment("CENTER")
                                )


                )
                .setFields("*")

                .setRange(gridRange.setSheetId(Db.grid))
        );


    }

    public static Request dropList(GridRange gridRange, String range) {
        System.out.println("range = " + range);
        System.out.println("gridRange = " + gridRange);
        System.out.println("Working drop list method !!!!!!!!!!!!! ");
        return new Request().setSetDataValidation(
                new SetDataValidationRequest()
                        .setRange(gridRange)
                        .setRule(
                                new DataValidationRule()
                                        .setStrict(true)
                                        .setCondition(
                                                new BooleanCondition()
                                                        .setType("ONE_OF_RANGE")
                                                        .setValues(List.of(
                                                                        new ConditionValue()
                                                                                .setUserEnteredValue(range)


                                                                )


                                                        )


                                        )

                                        .setShowCustomUi(true)


                        )
        );
    }


    public Request filter() {
        return new Request().setSetBasicFilter(
                new SetBasicFilterRequest()
                        .setFilter(
                                new BasicFilter()
                                        .setRange(
                                                new GridRange()
                                                        .setSheetId(Db.grid)
                                                        .setStartRowIndex(0)
                                                        .setStartColumnIndex(0)
                                                        .setEndColumnIndex(14)

                                        )

                        )

        );
    }


    public static boolean checkIndex(int index) {
        List<List<Object>> values = getValues(index, "A%s:D%s");


        if (Objects.isNull(values)) {

            return true;
        } else {
            return false;
        }

    }

    public static List<List<Object>> getValues(int index) throws IOException, GeneralSecurityException {

        return getValues(index, "A%s:P%s");
    }

    public static List<List<Object>> getValues(String range) throws IOException, GeneralSecurityException {
        Sheets service = getSheets();
        ValueRange response = service.spreadsheets().values()
                .get(sheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        return values;
    }

    public static List<List<Object>> getValues(int index, String range) {
        Sheets service = getSheets();
        ValueRange response = getExecute(index, range, service);
        List<List<Object>> values = response.getValues();
        return values;
    }

    private static ValueRange getExecute(int index, String range, Sheets service) {
        try {
            return service.spreadsheets().values()
                    .get(sheetId, range.formatted(index, index))
                    .execute();
        } catch (IOException e) {
            return getExecute(index, range, service);
        }
    }
}
