package uz.shohimardon.googlesheettask.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.shohimardon.googlesheettask.dto.DropListData;
import uz.shohimardon.googlesheettask.dto.InsertDto;
import uz.shohimardon.googlesheettask.dto.SheetDto;
import uz.shohimardon.googlesheettask.events.*;
import uz.shohimardon.googlesheettask.listeners.SetColorListener;
import uz.shohimardon.googlesheettask.service.google.GoogleService;
import uz.shohimardon.googlesheettask.utils.Action;
import uz.shohimardon.googlesheettask.utils.Db;
import uz.shohimardon.googlesheettask.validator.UpdateValidator;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static uz.shohimardon.googlesheettask.utils.Action.FirstColum;
import static uz.shohimardon.googlesheettask.utils.Db.index;
import static uz.shohimardon.googlesheettask.utils.Db.sheetId;

@Service
@RequiredArgsConstructor
public class UpdateService {
    private final UpdateValidator updateValidator;

    private final ApplicationEventPublisher eventPublisher;

    private final SetColorListener setColorListener;
    private final GoogleService googleService;

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void scheduleTaskUsingCronExpression() throws GeneralSecurityException, IOException {
        InsertDto insertDto;

        if (Db.insertDtoList.size() > 0) {

            System.out.println("Working  Scheduled ");
            System.out.println(LocalDateTime.now());
            insertDto = Db.insertDtoList.remove(0);

            System.out.println("Db.insertDtoList.size() = " + Db.insertDtoList.size());
            insertDate(insertDto);

        }

// Thread.sleep();
    }


    @Scheduled(fixedDelay = 5
            , timeUnit = TimeUnit.SECONDS)
    public void scheduleTask() throws GeneralSecurityException, IOException {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("Thread.getAllStackTraces().size() = " + Thread.getAllStackTraces().size());

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void checkAndStore(InsertDto dto) throws GeneralSecurityException, IOException {
        updateValidator.validOnInsert(dto);
        Db.insertDtoList.add(dto);

    }

    public void insertDate(InsertDto dto) throws GeneralSecurityException, IOException {

        Runnable runnable = null;
        new Thread(getTask(dto)).start();
//        new Thread(writeList(dto));    working getTask inside
//        new Thread(setColor()).start();   done write task inside
        Thread thread = null;
        try {
            runnable = writeList(dto);
            thread = new Thread(runnable);
            thread.start();
            thread.join();

        } catch (GeneralSecurityException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            try {
                Thread.sleep(5);
                thread.start();
            } catch (InterruptedException ex) {
                thread.start();
            }

        }

    }



    @Scheduled(zone = "Asia/Tashkent", cron = "0 0 0 1 1/1 *")
    @PostConstruct
    public void setColor() throws InterruptedException {


        System.out.println("Working Post construct");
        List<SetColorEvent> setColorEvents = new ArrayList<>();


        //        eventPublisher .;
        setColorEvents.add(SetColorEvent.builder()

                .color(
                        new Color()
                                .setRed(0.50806314f)
                                .setGreen(133.14041f)
                                .setBlue(13.697794f)
                )
                .text("NO")
                .grid(new GridRange()
                        .setSheetId(Db.grid)
                        .setStartColumnIndex(9)
                        .setEndColumnIndex(10)
                        .setStartRowIndex(1)
                        .setEndRowIndex(1994))
                .success(true)
                .build());
//            Yes

        setColorEvents.add(SetColorEvent.builder()

                .color(
                        new Color()
                                .setRed(154.05988f)
                                .setGreen(209.73622f)
                                .setBlue(54.28547f)
                )
                .text("Yes")
                .success(true)
                .grid(new GridRange()
                        .setSheetId(Db.grid)
                        .setStartColumnIndex(9)
                        .setEndColumnIndex(10)
                        .setStartRowIndex(0)
                        .setEndRowIndex(1994)

                ).build());
//            Coming
        setColorEvents.add(SetColorEvent.builder()

                .color(
                        new Color()
                                .setRed(136.52945f)
                                .setGreen(127.09175f)
                                .setBlue(192.8507f)
                )
                .text("Coming")
                .success(true)
                .grid(new GridRange()
                        .setSheetId(Db.grid)
                        .setStartColumnIndex(9)
                        .setEndColumnIndex(10)
                        .setStartRowIndex(0)
                        .setEndRowIndex(1994)

                ).build());
//  Attend
        setColorEvents.add(SetColorEvent.builder()

                .color(
                        new Color()
                                .setRed(37.175167f)
                                .setGreen(174.7474f)
                                .setBlue(16.875397f)
                )
                .text("Attend")
                .success(true)
                .grid(new GridRange()
                        .setSheetId(Db.grid)
                        .setStartColumnIndex(9)
                        .setEndColumnIndex(10)
                        .setStartRowIndex(0)
                        .setEndRowIndex(1994)

                ).build());

        //  Didn't pick up
        setColorEvents.add(SetColorEvent.builder()

                .color(
                        new Color()
                                .setRed(165.59735f)
                                .setGreen(169.93471f)
                                .setBlue(249.1229f)
                )
                .text("Didn't pick up")
                .success(true)
                .grid(new GridRange()
                        .setSheetId(Db.grid)
                        .setStartColumnIndex(9)
                        .setEndColumnIndex(10)
                        .setStartRowIndex(0)
                        .setEndRowIndex(1994)

                ).build());

        //  Switched off
        setColorEvents.add(SetColorEvent.builder()

                .color(
                        new Color()
                                .setRed(131.44212f)
                                .setGreen(11.190855f)
                                .setBlue(249.75388f)
                )
                .text("Switched off")
                .success(true)
                .grid(new GridRange()
                        .setSheetId(Db.grid)
                        .setStartColumnIndex(9)
                        .setEndColumnIndex(10)
                        .setStartRowIndex(0)
                        .setEndRowIndex(1994)

                ).build());
        //  Later
        setColorEvents.add(SetColorEvent.builder()

                .color(
                        new Color()
                                .setRed(19.04049f)
                                .setGreen(56.279587f)
                                .setBlue(22.343317f)
                )
                .text("Later")
                .success(true)
                .grid(new GridRange()
                        .setSheetId(Db.grid)
                        .setStartColumnIndex(9)
                        .setEndColumnIndex(10)
                        .setStartRowIndex(0)
                        .setEndRowIndex(1994)

                ).build());

        //  just added
        setColorEvents.add(SetColorEvent.builder()

                .color(
                        new Color()
                                .setRed(2.6073136f)
                                .setGreen(95.25657f)
                                .setBlue(101.43877f)
                )
                .text("Just added")
                .success(true)
                .grid(new GridRange()
                        .setSheetId(Db.grid)
                        .setStartColumnIndex(9)
                        .setEndColumnIndex(10)
                        .setStartRowIndex(0)
                        .setEndRowIndex(1994)

                ).build());

        for (SetColorEvent event : setColorEvents) {

            setColorListener.setColor(event);
        }
    }

    private Runnable getTask(InsertDto dto) {
        Runnable task = () -> {


            String sheetId = Db.sheetId;

            Map<Action, Boolean> actionBooleanMap = Db.actionsMap.get(sheetId);


            Boolean isResize = actionBooleanMap.get(Action.Resize);


            isResize(isResize);


            Boolean firstRow = actionBooleanMap.get(FirstColum);


            new Thread(() -> {
                try {
                    isDoneFirstRow(firstRow, actionBooleanMap);
                } catch (GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();


            try {
                writeDate(dto);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        };

        return task;
    }

    private synchronized Runnable writeList(InsertDto dto) throws GeneralSecurityException, IOException {
        return () -> {
//  Course

            String course = "=Data!H1:H10";


            int i = index.get(sheetId).get();

            System.out.println("i = " + i);
            int endRowIndex = i + 1;
            eventPublisher.publishEvent(new DropListEvent(
                    new DropListData(new GridRange()
                            .setSheetId(Db.grid)
                            .setStartColumnIndex(4)
                            .setEndColumnIndex(5)
                            .setStartRowIndex(i)
                            .setEndRowIndex(endRowIndex), course

                    ), true
            ));


            new Thread(() -> {

                setDropListText(dto.getCourse(), "E");

            }).start();


//           Branch
            System.out.println();
            System.out.println("Add branch drop list");
            System.out.println("i = " + i);
            eventPublisher.publishEvent(new DropListEvent(
                    new DropListData(new GridRange()
                            .setSheetId(Db.grid)
                            .setStartColumnIndex(5)
                            .setEndColumnIndex(6)
                            .setStartRowIndex(i)
                            .setEndRowIndex(endRowIndex)
                            , "=Data!I1:I8"

                    ), true
            ));
            new Thread(() -> {


                setDropListText(dto.getBranch(), "F");

            }).start();


            //           Week day
            eventPublisher.publishEvent(new DropListEvent(
                    new DropListData(new GridRange()
                            .setSheetId(Db.grid)
                            .setStartColumnIndex(6)
                            .setEndColumnIndex(7)
                            .setStartRowIndex(i)
                            .setEndRowIndex(endRowIndex), "=Data!J1:J2"

                    ), true
            ));
//        Time

            eventPublisher.publishEvent(new DropListEvent(
                    new DropListData(new GridRange()
                            .setSheetId(Db.grid)
                            .setStartColumnIndex(7)
                            .setEndColumnIndex(8)
                            .setStartRowIndex(i)
                            .setEndRowIndex(endRowIndex), "=Data!K1:K34"

                    ), true
            ));
//        Status

            eventPublisher.publishEvent(new DropListEvent(
                    new DropListData(new GridRange()
                            .setSheetId(Db.grid)
                            .setStartColumnIndex(9)
                            .setEndColumnIndex(10)
                            .setStartRowIndex(i)
                            .setEndRowIndex(endRowIndex), "=Data!A1:A9"

                    ), true
            ));
//
            Thread thread = new Thread(() -> {

                setDropListText("Just added", "J");

            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {

                thread.start();
            }

            eventPublisher.publishEvent(new DropListEvent(
                    new DropListData(new GridRange()
                            .setSheetId(Db.grid)
                            .setStartColumnIndex(11)
                            .setEndColumnIndex(10)
                            .setStartRowIndex(i)
                            .setEndRowIndex(i), "=Data!I1:I8"

                    ), true
            ));
        };

    }

    public static void setDropListText(String data, String column) {
        Sheets service = GoogleService.getSheets();
        Sheets.Spreadsheets.Values.Update raw = null;
        try {
            raw = service.spreadsheets().values().update(sheetId, column + (index.get(sheetId).get()), new ValueRange().setValues(List.of(List.of(data)))).setValueInputOption("RAW");

            System.out.println(index.get(sheetId).get());
            raw.execute();
        } catch (Exception e) {

            Db.setDropListTextList.put(data, column);


        }


    }

    private synchronized void writeDate(InsertDto dto) throws InterruptedException {
        SheetDto sheet = new SheetDto();
        Map<String, String> d = sheet.getData();


        AtomicInteger index = Db.index.get(sheetId);


        boolean isFreeIndex = false;
        boolean isIcraese = false;
        while (!isFreeIndex) {


            isFreeIndex = GoogleService.checkIndex(index.get());


            System.out.println("isFreeIndex = " + isFreeIndex);

            if (!isFreeIndex) {
                isIcraese = true;
                index.getAndIncrement();
            }
            if (isFreeIndex) {
                break;

            }
        }

        if (isIcraese) {
            index.decrementAndGet();
        }


        Db.isBusyIndex.put(index.get(), true);


        d.put("A" + (index.get() + 1), index.get() + ".");

        d.put("B" + (index.get() + 1), LocalDateTime.now(ZoneId.of("Asia/Tashkent")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString());
        d.put("C" + (index.get() + 1), dto.getFullName());
        d.put("D" + (index.get() + 1), dto.getPhone());


        sheet.setIndex(index.getAndIncrement());


        GenericSpringEvent<SheetDto> springEvents = new GenericSpringEvent<>(sheet, true);

        try {
            new Thread(() -> {
                eventPublisher.publishEvent(springEvents);
            }).start();
        } catch (Exception e) {
            Db.leftWriteList.add(springEvents);

        }
    }


    private void isDoneFirstRow(Boolean firstRow, Map<Action, Boolean> actionBooleanMap) throws
            GeneralSecurityException, IOException {


        // TODO: 11/26/22
        if (!firstRow) {


            Optional<SheetDto> optional = getFirstRow();


            SheetDto sheetDto = optional.get();


            eventPublisher.publishEvent(
                    new FirstRowEvent(
                            sheetDto, true

                    )
            );
        }

    }

    private Optional<SheetDto> getFirstRow() throws GeneralSecurityException, IOException {


        SheetDto sheet = new SheetDto();
        sheet.setIndex(0);
        Map<String, String> data = sheet.getData();
        int i = 65;


        for (String s : Db.firstListDate) {

            char c = (char) i;


            data.put(c + "1", s);

            i += 1;
        }

        return Optional.of(sheet);
    }


    private void isResize(Boolean isResize) {
        if (!isResize) {


            new Thread(() -> {
                eventPublisher.publishEvent(new FirstColumStyleEvent(
                        true
                ));
            }).start();


            eventPublisher.publishEvent(new ResizeEvent(
                    Db.index.get(sheetId).get(), true
            ));


        }


    }


}
