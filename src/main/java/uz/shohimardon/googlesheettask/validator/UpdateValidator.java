package uz.shohimardon.googlesheettask.validator;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.dto.InsertDto;
import uz.shohimardon.googlesheettask.utils.Db;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static uz.shohimardon.googlesheettask.service.google.GoogleService.getSheets;
import static uz.shohimardon.googlesheettask.utils.Db.sheetId;

@Component
public class UpdateValidator {
    public void validOnInsert(InsertDto dto) {

        Objects.requireNonNull(dto);
        Objects.requireNonNull(dto.getCourse());

        Objects.requireNonNull(dto.getPhone());

        if (dto.getPhone().isBlank()) {
            throw new NullPointerException("Phone number can't be blink");
        }
        if (dto.getFullName().isBlank()) {
            throw new NullPointerException("FullName number can't be blink");
        }
        if (dto.getCourse().isBlank()) {
            throw new NullPointerException("Course number can't be blink");
        }
        String course = dto.getCourse();
        String aThrow = Db.courseList.stream().filter(s -> s.equalsIgnoreCase(course)).findAny().orElseThrow(
                () -> {
                    throw new NullPointerException("Course not found  by %s ".formatted(course));
                }
        );
        String branch = Db.branchList.stream().filter(s -> s.equalsIgnoreCase(dto.getBranch())).findAny().orElseThrow(
                () -> {
                    throw new NullPointerException("Branch not found  by %s ".formatted(dto.getBranch()));
                }
        );


        boolean isSavePhone = isSave(dto.getPhone(), "D");
        boolean isSaveName = isSave(dto.getFullName(), "C");

        if (isSavePhone && isSaveName) {
            throw new NullPointerException("This %s -> fullName  and this phone -> %s already saved ".formatted(dto.getFullName(), dto.getPhone()));
        }


    }

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

    public static void main(String[] args) {
        System.out.println(Pattern.matches("^\\+998?(9[0-9]|6[125679]|7[01234569])[0-9]{7}$", "+998952035218"));//true (a or m or n comes one time)


    }
}
