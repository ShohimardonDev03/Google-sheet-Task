package uz.shohimardon.googlesheettask.listeners;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.events.SetColorEvent;
import uz.shohimardon.googlesheettask.service.google.GoogleService;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SetColorListener {
    private final GoogleService googleService;
    private final ApplicationEventPublisher applicationEventPublisher;


//    @EventListener(classes = SetColorEvent.class, condition = "#event.success")
    public void setColor(SetColorEvent event) throws InterruptedException {
        String text = event.getText();
        GridRange gridRange = event.getGrid();
        System.out.println("event = " + event);
        Color color = event.getColor();

        List<Request> requests = new ArrayList<>();
        System.out.println(event.getColor()
        );
        requests.add(new Request()
                .setAddConditionalFormatRule(new AddConditionalFormatRuleRequest()


//                        .setIndex(1)/

                        .setRule(
                                new ConditionalFormatRule()
                                        .setRanges(List.of(
                                                gridRange


                                        ))
                                        .setBooleanRule(new BooleanRule()
                                                .setCondition(new BooleanCondition()
                                                        .setValues(List.of(new ConditionValue()

                                                                .setUserEnteredValue(text)))
                                                        .setType("TEXT_CONTAINS"))


                                                .setFormat(
                                                        new CellFormat()

                                                                .setBackgroundColor(
                                                                        color
                                                                )
                                                ))


                        ))


        );

        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
        batchUpdateSpreadsheetRequest.setRequests(requests);


        Sheets.Spreadsheets.BatchUpdate batchUpdate = null;
        if (requests.size() > 0) {
            try {
                GoogleService.getSheets().spreadsheets().batchUpdate(Db.sheetId, batchUpdateSpreadsheetRequest).execute();
            } catch (IOException e) {

                Db.setColorEventList.add(event);
            }
        }
    }
}
