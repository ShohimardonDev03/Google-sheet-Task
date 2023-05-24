package uz.shohimardon.googlesheettask.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SheetDto {
    private Map<String, String> data = new HashMap<>();
    private Integer index;
//   private String sheetId;
//   private String range;
//   private String value;
}
