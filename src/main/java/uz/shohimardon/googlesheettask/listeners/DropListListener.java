package uz.shohimardon.googlesheettask.listeners;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.dto.DropListData;
import uz.shohimardon.googlesheettask.dto.SheetDto;
import uz.shohimardon.googlesheettask.events.DropListEvent;
import uz.shohimardon.googlesheettask.events.GenericSpringEvent;
import uz.shohimardon.googlesheettask.service.google.GoogleService;
import uz.shohimardon.googlesheettask.utils.Action;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uz.shohimardon.googlesheettask.service.google.GoogleService.getSheets;
import static uz.shohimardon.googlesheettask.service.google.GoogleService.getValues;
import static uz.shohimardon.googlesheettask.utils.Db.sheetId;

@RequiredArgsConstructor
@Component
public class DropListListener {

    private final GoogleService googleService;
    private final ApplicationEventPublisher applicationEventPublisher;


    @EventListener(classes = DropListEvent.class, condition = "#event.success")
    public void dropList(DropListEvent event) {
        try {
            DropListData what = event.getWhat();
            Sheets sheets = getSheets();
            sheets.spreadsheets().values();
            boolean data = false;
            Spreadsheet s;
            try {
                s = sheets.spreadsheets().get(Db.sheetId).execute();
            } catch (Exception e) {

                Thread.sleep(60_000);
                Db.lefDropListEventList.add(event);
                return;
            }
            for (Sheet sheet : s.getSheets()) {
                data = sheet.getProperties().getTitle().equals("Data");
            }
            if (!data) {
                createDataPage("Data");

                addCurseList();


                addBRANCH();

                addWEEK_DAY();

                addSTATUS();


                AddTime();


            }


            try {
                check();

            } catch (Exception e) {
                System.out.println("Added to leftDropListEventList ");
                System.out.println("Db.lefDropListEventList.size() = " + Db.lefDropListEventList.size());
                System.out.println("e.getMessage() = " + e.getMessage());
                Thread.sleep(60_000);
                Db.lefDropListEventList.add(event);
                return;
            }


            Request request = googleService.dropList(what.gridRange(), what.range());

            BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
            batchUpdateSpreadsheetRequest.setRequests(List.of(request));

            final Sheets.Spreadsheets.BatchUpdate batchUpdate = getSheets().
                    spreadsheets().batchUpdate(Db.sheetId, batchUpdateSpreadsheetRequest);
            try {


                batchUpdate.execute();

                System.out.println("    Execute drop list");
                System.out.println("");
            } catch (Exception e) {

                System.out.println("Added to leftDropListEventList ");
                System.out.println("e.getMessage() = " + e.getMessage());

                System.out.println("Db.lefDropListEventList.size() = " + Db.lefDropListEventList.size());
                Thread.sleep(60_000);
                Db.lefDropListEventList.add(event);
            }
        } catch (GeneralSecurityException | IOException | InterruptedException e) {
            System.out.println("Added to leftDropListEventList ");
            System.out.println("Db.lefDropListEventList.size() = " + Db.lefDropListEventList.size());

            e.printStackTrace();
        }


    }

    private void check() {


        Boolean branch = Db.actionsMap.get(sheetId)
                .get(Action.DirectList);


        String branchRange = "Data!I1:I8";
        String courseRange = "Data!H1:H10";
        String weakRange = "Data!J1:J2";
        String timeRange = "Data!K1:K34";
        String statusRange = "Data!A1:A9";


        List<List<Object>> values = null;
        if (!branch) {

            try {
                values = getValues(branchRange);
            } catch (IOException | GeneralSecurityException e) {
                try {
                    Thread.sleep(20);
                    values = getValues(branchRange);
                } catch (Exception ex) {
                    System.out.println(e.getMessage());
                }
            }

            if (values.size() < 5)
                addBRANCH();
        }
        Boolean course = Db.actionsMap.get(sheetId)
                .get(Action.BranchList);

        if (!course) {


            values = null;

            try {
                values = getValues(courseRange);
            } catch (IOException | GeneralSecurityException e) {
                try {
                    Thread.sleep(20);
                    values = getValues(courseRange);
                } catch (Exception ex) {
                    System.out.println(e.getMessage());
                }
            }

            if (values.size() < 5)


                addCurseList();


        }
        Boolean timelist = Db.actionsMap.get(sheetId)
                .get(Action.TimeList);


        if (!timelist) {

            values = null;

            try {
                values = getValues(timeRange);
            } catch (IOException | GeneralSecurityException e) {
                try {
                    Thread.sleep(20);
                    values = getValues(timeRange);
                } catch (Exception ex) {
                    System.out.println(e.getMessage());
                }
            }

            if (values.size() < 5)
                AddTime();
        }


        Boolean StatusList = Db.actionsMap.get(sheetId)
                .get(Action.StatusList);


        if (!StatusList) {


            values = null;

            try {
                values = getValues(statusRange);
            } catch (IOException | GeneralSecurityException e) {
                try {
                    Thread.sleep(20);
                    values = getValues(statusRange);
                } catch (Exception ex) {
                    System.out.println(e.getMessage());
                }
            }

            if (values.size() < 5)

                addSTATUS();

        }

        Boolean WeeKDayList = Db.actionsMap.get(sheetId)
                .get(Action.WeeKDayList);


        if (!WeeKDayList) {


            values = null;

            try {
                values = getValues(weakRange);
            } catch (IOException | GeneralSecurityException e) {
                try {
                    Thread.sleep(20);
                    values = getValues(weakRange);
                } catch (Exception ex) {
                    System.out.println(e.getMessage());
                }
            }

            if (values.size() < 5)
                addWEEK_DAY();

        }
    }

    public void AddTime() {

        List<String> list = List.of(
                "AFTER LUNCH",
                "BEFORE LUNCH",
                "EVENING",
                "6:00:00",
                "6:30:00",
                "7:00:00",
                "7:30:00",
                "8:00:00",
                "8:30:00",
                "9:00:00",
                "9:30:00",
                "10:00:00",
                "10:30:00",
                "11:00:00",
                "11:30:00",
                "12:00:00",
                "12:30:00",
                "13:00:00",
                "13:30:00",
                "14:00:00",
                "14:30:00",
                "15:00:00",
                "15:30:00",
                "16:00:00",
                "16:30:00",
                "17:00:00",
                "17:30:00",
                "18:00:00",
                "18:30:00",
                "19:00:00",
                "19:30:00",
                "20:00:00",
                "20:30:00",
                "ANY"

        );
        Db.actionsMap.get(sheetId).put(Action.TimeList, true);
        genericInsertList(list, 'K');
    }


    public void addSTATUS() {
        List<String> list = List.of(
                "Yes",
                "No",
                "Coming",
                "Attend",
                "Didn't pick up",
                "Switched off",
                "Later",

                "Just added",
                "MYS student"

        );
        genericInsertList(list, 'A');
        Db.actionsMap.get(sheetId).put(Action.StatusList, true);
    }

    public void addWEEK_DAY() {
        List<String> list = List.of(

                "M/W/F",
                "T/T/S"

        );
        genericInsertList(list, 'J');
        Db.actionsMap.get(sheetId).put(Action.WeeKDayList, true);
    }

    public void addBRANCH() {
        ;
        genericInsertList(Db.branchList, 'I');
        Db.actionsMap.get(sheetId).put(Action.DirectList, true);
        Db.actionsMap.get(sheetId).put(Action.BranchList, true);
    }

    public void addCurseList() {


        Db.actionsMap.get(sheetId).put(Action.CourseList, true);
        genericInsertList(Db.courseList, 'H');

    }

    public void genericInsertList(List<String> list, char c) {
        SheetDto sheet = new SheetDto();
        Map<String, String> d = sheet.getData();


        for (int i = 0; i < list.size(); i++) {

            String key = "Data!" + c + (i + 1);
            d.put(key, list.get(i));


        }

        sheet.setIndex(1);

        GenericSpringEvent<SheetDto> springEvents = new GenericSpringEvent<>(sheet, false);

        applicationEventPublisher.publishEvent(springEvents);

    }

    public static String createDataPage(String name) throws GeneralSecurityException, IOException {


        List<Request> requestList = new ArrayList<>();


        requestList.add(new Request()
                .setAddSheet(
                        new AddSheetRequest()
                                .setProperties(
                                        new SheetProperties()
                                                .setTitle(name)


                                )
                ));


        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
        batchUpdateSpreadsheetRequest.setRequests(requestList);

        return getSheets().spreadsheets().batchUpdate(Db.sheetId, batchUpdateSpreadsheetRequest).execute().getSpreadsheetId();
    }


}
