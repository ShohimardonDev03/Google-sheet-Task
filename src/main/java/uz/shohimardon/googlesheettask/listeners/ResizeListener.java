package uz.shohimardon.googlesheettask.listeners;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.events.ResizeEvent;
import uz.shohimardon.googlesheettask.service.google.GoogleService;
import uz.shohimardon.googlesheettask.utils.Action;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResizeListener {
    private final GoogleService googleService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public static final String rows = "ROWS";
    public final String columns = "COLUMNS";

    @EventListener(classes = ResizeEvent.class, condition = "#event.success")
    public void resize(ResizeEvent event) {

        ArrayList<Request> requests = new ArrayList<>();
        GridRange gridRange = new GridRange();


        gridRange.setSheetId(Db.grid);


        System.out.println("\n \n ");
        System.out.println("is Resize Listener");
        System.out.println(LocalDateTime.now());


//        First Row
        requests.add(GoogleService.size(
                GoogleService.getRange(gridRange.getSheetId(),
                        1, 0, rows), 35
        ));
//        Zero Row
        requests.add(GoogleService.size(
                GoogleService.getRange(gridRange.getSheetId(),
                        1, 0, columns), 27
        ));

        requests.add(GoogleService.size(
                googleService.getRange(gridRange.getSheetId(),
                         1, columns), 145
        ));

//        Column

        requests.add(GoogleService.size(
                GoogleService.getRange(gridRange.getSheetId(),
                        10, 9, columns), 237
        ));

        requests.add(GoogleService.size(
                GoogleService.getRange(gridRange.getSheetId(),
                        14, 13, columns), 366
        ));
        gridRange.setSheetId(Db.grid);

        Request size = GoogleService.size(
                GoogleService.getRange(gridRange.getSheetId(),
                        event.getWhat()+1, event.getWhat() - 1, rows), 27
        );


        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
        batchUpdateSpreadsheetRequest.setRequests(requests);

        Sheets.Spreadsheets.BatchUpdate batchUpdate = null;
        try {







            batchUpdate = GoogleService.getSheets().spreadsheets().batchUpdate(Db.sheetId, new BatchUpdateSpreadsheetRequest().setRequests(Collections.singletonList(size)));

            batchUpdate.execute();



        } catch (Exception e) {

            System.out.println("Error");
            Db.leftBatchUpdateList.add(batchUpdate);

        }

        try {
            if (requests.size() > 0) {
                batchUpdate = GoogleService.getSheets().spreadsheets().batchUpdate(Db.sheetId, batchUpdateSpreadsheetRequest);
                batchUpdate.execute();
            }
            Db.actionsMap.get(Db.sheetId).put(Action.Resize, true);


        } catch (IOException e) {

            Db.actionsMap.get(Db.sheetId).put(Action.Resize, false);
            applicationEventPublisher.publishEvent(event);
        }




    }


}
