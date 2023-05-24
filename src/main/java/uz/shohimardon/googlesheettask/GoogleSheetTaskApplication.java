package uz.shohimardon.googlesheettask;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import uz.shohimardon.googlesheettask.utils.Action;
import uz.shohimardon.googlesheettask.utils.Db;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static uz.shohimardon.googlesheettask.utils.Db.sheetId;

@SpringBootApplication
@EnableScheduling
public class GoogleSheetTaskApplication {

    public static void main(String[] args) {


        SpringApplication.run(GoogleSheetTaskApplication.class, args);
    }

    @PostConstruct
    public void init() {

        Map<String, Map<Action, Boolean>> actionsMap = Db.actionsMap;
        HashMap<Action, Boolean> items = new HashMap<>();



        items.put(Action.Resize, false);

        items.put(Action.BranchList, false);

        items.put(Action.CourseList, false);

        items.put(Action.WeeKDayList, false);

        items.put(Action.FirstColum, false);

        items.put(Action.TimeList, false);

        items.put(Action.StatusList, false);

        items.put(Action.DirectList, false);

        actionsMap.put(sheetId,items);

        Db.index.put(sheetId, new AtomicInteger(1));

    }

}
