package uz.shohimardon.googlesheettask.listeners;


import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SetBasicFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.events.FirstColumStyleEvent;
import uz.shohimardon.googlesheettask.events.ResizeEvent;
import uz.shohimardon.googlesheettask.service.google.GoogleService;
import uz.shohimardon.googlesheettask.utils.Action;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class FirstColumnListener {

    private final GoogleService googleService;
    private final ApplicationEventPublisher applicationEventPublisher;


    @EventListener(classes = FirstColumStyleEvent.class, condition = "#event.success")
    public void firsColumnListener(FirstColumStyleEvent event) {

        Request request = GoogleService.updateColumnStyle(
                new GridRange()
                        .setStartRowIndex(0)
                        .setEndRowIndex(1)

                        .setStartColumnIndex(0)
                , 0, 0, 0, 84.26894f, 219.80359f, 165.47551f
        );

        ArrayList<Request> requestList = new ArrayList<>();

        requestList.add(request);


        requestList.add(googleService.filter());


        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
        batchUpdateSpreadsheetRequest.setRequests(requestList);


        Sheets.Spreadsheets.BatchUpdate batchUpdate = null;
        try {

            batchUpdate = GoogleService.getSheets().spreadsheets().batchUpdate(Db.sheetId, batchUpdateSpreadsheetRequest);
            batchUpdate.execute();

            Db.actionsMap.get(Db.sheetId).put(Action.Resize, true);


        } catch (IOException e) {

            Db.actionsMap.get(Db.sheetId).put(Action.Resize, false);
            applicationEventPublisher.publishEvent(event);
        }


    }

}
