package uz.shohimardon.googlesheettask.listeners;

import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.events.StyleEvent;
import uz.shohimardon.googlesheettask.service.google.GoogleService;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StyleListener {
    private final GoogleService googleService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener(classes = StyleEvent.class, condition = "#event.success")
    public void makeStyle(StyleEvent event) throws InterruptedException, GeneralSecurityException, IOException {


        {

            int index = event.getWhat();


            boolean isMakeTable = false;

            while (!isMakeTable) {

                if (!GoogleService.checkIndex(index)) {
                    index += 1;
                } else {
                    isMakeTable = true;
                }
            }


            System.out.println("\n\n ");
            System.out.println("is Style listener");
            System.out.println("index = " + index);


            List<Request> requestList = new ArrayList<>();


            for (int i = index; i < index + 60; i++) {


                requestList.add(GoogleService.updateColumnStyle(
                        new GridRange()
                                .setSheetId(Db.grid)
                                .setStartRowIndex(i)


                                .setStartColumnIndex(0)

                ));



            }


            BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
            batchUpdateSpreadsheetRequest.setRequests(requestList);
            try {
                GoogleService.getSheets().spreadsheets().batchUpdate(Db.sheetId, batchUpdateSpreadsheetRequest).execute();

                System.out.printf("Execute from %s to %s \n ", index, index + 60);

                for (int t = 0; t < 59; t++) {


                    Db.isMakeTable.put(index + t, true);
                }


            } catch (Exception e) {

                try {

                    System.out.println("Style Listener error 1");

                    applicationEventPublisher.publishEvent(event);

                } catch (Exception e1) {
                    System.out.println("Style Listener error 2");

                    applicationEventPublisher.publishEvent(event);

                }
            }


        }
    }
}