package uz.shohimardon.googlesheettask;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uz.shohimardon.googlesheettask.dto.DropListData;
import uz.shohimardon.googlesheettask.events.DropListEvent;
import uz.shohimardon.googlesheettask.service.google.GoogleService;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

import static uz.shohimardon.googlesheettask.service.google.GoogleService.*;
import static uz.shohimardon.googlesheettask.utils.Db.*;

@SpringBootTest
class GoogleSheetTaskApplicationTests {

    public boolean isSave(String target, String range) {
        ValueRange execute = null;
        try {



                execute = getSheets().spreadsheets().values()
                        .get(sheetId, "%s2:%s10000".formatted(range, range))
                        .execute();

        } catch (IOException e) {
            try {
                e.printStackTrace();
                Thread.sleep(3_000);
                isSave(target, range);
            } catch (InterruptedException ex) {
                e
                        .getMessage();
            }
        }

        if (Objects.isNull(execute.getValues())) {

            System.out.println((Object) null);
            return false;
        }
        for (List<Object> value : execute.getValues()) {
            return value.toString().equalsIgnoreCase(target);

        }

        return false;
    }

    @Test
    void test() {

        isSave("A", "A");
    }
}