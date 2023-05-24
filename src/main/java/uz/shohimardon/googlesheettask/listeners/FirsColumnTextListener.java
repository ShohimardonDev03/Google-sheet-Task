package uz.shohimardon.googlesheettask.listeners;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.dto.SheetDto;
import uz.shohimardon.googlesheettask.events.FirstRowEvent;
import uz.shohimardon.googlesheettask.service.google.GoogleService;
import uz.shohimardon.googlesheettask.utils.Action;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class FirsColumnTextListener {

    private final GoogleService googleService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener(classes = FirstRowEvent.class, condition = "#event.success")
    public void resize(FirstRowEvent event) throws GeneralSecurityException, IOException {

        SheetDto what = null;

        what = event.getWhat();


        Sheets service = GoogleService.getSheets();

        String sheetId = Db.sheetId;


        what.getData().forEach((range, value) -> {


            SheetDto sheetDto = new SheetDto();
            ValueRange valueRange = new ValueRange().setValues(List.of(List.of(value)));
            try {


                service.spreadsheets().values().update(sheetId, range, valueRange).setValueInputOption("RAW").execute();

                Db.actionsMap.get(sheetId).put(Action.FirstColum, true);

            } catch (IOException e) {


                    sheetDto.setIndex(event.getWhat().getIndex());
                    sheetDto.getData().put(range, value);








                    Db.actionsMap.get(sheetId).put(Action.FirstColum, true);

                    applicationEventPublisher.publishEvent(new FirstRowEvent(sheetDto, true));

//                    ex.printStackTrace();

                    Db.actionsMap.get(sheetId).put(Action.FirstColum, true);
                    applicationEventPublisher.publishEvent(new FirstRowEvent(sheetDto, true));
                }
//            }
        });

    }
}
