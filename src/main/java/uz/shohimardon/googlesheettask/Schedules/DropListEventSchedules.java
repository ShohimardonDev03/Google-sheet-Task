package uz.shohimardon.googlesheettask.Schedules;

import com.google.api.services.sheets.v4.Sheets;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.dto.InsertDto;
import uz.shohimardon.googlesheettask.events.DropListEvent;
import uz.shohimardon.googlesheettask.service.UpdateService;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static uz.shohimardon.googlesheettask.utils.Db.isMakeTable;
import static uz.shohimardon.googlesheettask.utils.Db.setDropListTextList;

@Component
@RequiredArgsConstructor
public class DropListEventSchedules {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.SECONDS)
    public void scheduleTaskUsingCronExpression() throws GeneralSecurityException, IOException {
        InsertDto insertDto;

        if (Db.lefDropListEventList.size() > 0) {
            System.out.println("Db.lefDropListEventList.size() = " + Db.lefDropListEventList.size());
            System.out.println("****************************************88");

            for (DropListEvent dropListEvent : Db.lefDropListEventList) {
                System.out.println("dropListEvent = " + dropListEvent);
            }


            System.out.println("****************************************88");

            System.out.println("DropListEventSchedules  Scheduled ");
            System.out.println(LocalDateTime.now());


            applicationEventPublisher.publishEvent(Db.lefDropListEventList.remove(0));

        }
    }

    @Scheduled(fixedDelay = 4, timeUnit = TimeUnit.SECONDS)
    public void SetColorEvent() throws GeneralSecurityException, IOException {


        if (Db.setColorEventList.size() > 0) {

            System.out.println("DropListEventSchedules  Scheduled ");
            System.out.println(LocalDateTime.now());


            applicationEventPublisher.publishEvent((Db.setColorEventList.remove(0)));

        }
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void scheduleCronExpression() throws GeneralSecurityException, IOException {
        InsertDto insertDto;

        if (Db.leftUpdateList.size() > 0) {

            System.out.println("Drop List event scheduled");
            System.out.println("######################################################");
            System.out.println(Db.leftUpdateList.size());

            System.out.println("######################################################");


            System.out.println(Db.leftUpdateList.size());
            System.out.println(LocalDateTime.now());


            Db.leftUpdateList.remove(0).execute();

        }


        int size = setDropListTextList.size();
        if (size > 0) {
            int q = 0;
            for (Map.Entry<String, String> stringStringEntry : setDropListTextList.entrySet()) {
                int i = new Random().nextInt(0, size);
                if (q == i) {
                    String key = stringStringEntry.getKey();
                    UpdateService.setDropListText(key, stringStringEntry.getValue());
                    setDropListTextList.remove(key);
                    break;
                }

                q++;
            }
        }

        if (Db.leftBatchUpdateList.size() > 0) {

            Db.leftBatchUpdateList.remove(0).execute();
        }
        if (Db.leftWriteList.size() > 0) {
            applicationEventPublisher.publishEvent(Db.leftUpdateList.remove(0));
        }

    }


    @Scheduled(zone = "Asia/Tashkent", cron = "1 0 * * * *")
    public void refreshEveryMonth() {


    }
}
