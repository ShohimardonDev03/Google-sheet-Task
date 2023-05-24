package uz.shohimardon.googlesheettask.utils;

import com.google.api.services.sheets.v4.Sheets;

import uz.shohimardon.googlesheettask.dto.InsertDto;
import uz.shohimardon.googlesheettask.dto.SheetDto;
import uz.shohimardon.googlesheettask.events.DropListEvent;
import uz.shohimardon.googlesheettask.events.GenericSpringEvent;
import uz.shohimardon.googlesheettask.events.SetColorEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Db {
    public static Map<String, Map<Action, Boolean>> actionsMap = new HashMap<>();
    public static String sheetId = "1Fa8UmlwWQz9TqqEY01k-b8BqrN441lH4fsNXFaec85A";
    public static Map<Integer, Boolean> isMakeTable = new HashMap<>();
    public static List<DropListEvent> lefDropListEventList = new ArrayList<>();
    public static List<SetColorEvent> setColorEventList = new ArrayList<>();
    public static List<Sheets.Spreadsheets.Values.Update> leftUpdateList = new ArrayList<>();
    public static List<Sheets.Spreadsheets.BatchUpdate> leftBatchUpdateList = new ArrayList<>();

    public static List<GenericSpringEvent> leftWriteList = new ArrayList<>();
    public static Map<String, String> setDropListTextList = new HashMap<>();
    public static Integer grid = 0;

    public static List<InsertDto> insertDtoList = new ArrayList<>();

    public static Map<Integer, Boolean> isBusyIndex = new HashMap<>();

    public static List<String> firstListDate = List.of("", "Date", "FullName", "Phone Number"
            , "Course", "Branch", "Week days", "Time", "Coming date", "Status", "Call Date", "Directed", "Operator", "Comments", "Other");

    public volatile static Map<String, AtomicInteger> index = new HashMap<>();


    public static List<String> branchList = List.of(

            "Yunusobod",
            "Ganga",
            "Chilonzor",
            "Kosmonavtlar",
            "Ucell",
            "Navoiy",
            "Darxon",
            "Shakhriston"

    );
    public static List<String> courseList = List.of(
            "IELTS",
            "General English",

            "Intensiv",

            "Rus tili"
            ,
            "Logistika"
            ,
            "Speaking",

            "IELTS 3 oyda",

            "KIDS English"
            ,
            "Matematika");


}

