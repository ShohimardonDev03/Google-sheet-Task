package uz.shohimardon.googlesheettask.listeners;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.dto.SheetDto;
import uz.shohimardon.googlesheettask.events.GenericSpringEvent;
import uz.shohimardon.googlesheettask.events.StyleEvent;
import uz.shohimardon.googlesheettask.service.google.GoogleService;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static uz.shohimardon.googlesheettask.listeners.ResizeListener.rows;

@Component
@RequiredArgsConstructor
public class WriteTextListener {

    private final GoogleService googleService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener(classes = GenericSpringEvent.class, condition = "#event.success")
    public void write(@NonNull GenericSpringEvent<SheetDto> event) throws GeneralSecurityException, IOException, InterruptedException {

        SheetDto what = null;
        System.out.println("event = " + event);
        what = event.getWhat();

        Integer index = what.getIndex();


        Boolean isMakeTable = Db.isMakeTable.getOrDefault(index, false);


        if (!isMakeTable || !Db.isMakeTable.getOrDefault(index + 7, false)) {

            try {
                applicationEventPublisher.publishEvent(new StyleEvent(
                        index, true
                ));
            } catch (Exception e) {
                applicationEventPublisher.publishEvent(new GenericSpringEvent<SheetDto>(event.getWhat(), true));
                Db.leftWriteList.add(event);
            }

            System.out.println("Thread.currentThread() = " + Thread.currentThread());


        }


        Sheets service = GoogleService.getSheets();

        String sheetId = Db.sheetId;


        what.getData().forEach((range, value) -> {


            SheetDto sheetDto = new SheetDto();
            ValueRange valueRange = new ValueRange().setValues(List.of(List.of(value)));
            try {


                service.spreadsheets().values().update(sheetId, range, valueRange).setValueInputOption("RAW").execute();


                sheetDto.setIndex(index);
                sheetDto.getData().put(range, value);


            } catch (IOException e) {


                sheetDto.setIndex(index);
                sheetDto.getData().put(range, value);
                System.out.println(sheetDto);

                System.out.println(LocalDateTime.now());
                try {
                    Thread.sleep(new Random().nextLong(1_000, 4_000));
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                Db.leftWriteList.add(event);

                System.out.println("Write method WriteText Listener");
//                    ex.printStackTrace();
            }

        });


    }


    @EventListener(classes = GenericSpringEvent.class, condition = "#event.success==false")

    public void writes(@NonNull GenericSpringEvent<SheetDto> event) throws
            GeneralSecurityException, IOException, InterruptedException {
        Sheets service = GoogleService.getSheets();
        SheetDto what = null;
        System.out.println("event = " + event);
        what = event.getWhat();

        Integer index = what.getIndex();
        String sheetId = Db.sheetId;


        what.getData().forEach((range, value) -> {


            SheetDto sheetDto = new SheetDto();
            ValueRange valueRange = new ValueRange().setValues(List.of(List.of(value)));
            try {


                service.spreadsheets().values().update(sheetId, range, valueRange).setValueInputOption("RAW").execute();


                sheetDto.setIndex(index);
                sheetDto.getData().put(range, value);


            } catch (IOException e) {



                    Db.leftWriteList.add(event);
            }
        });
    }
}
