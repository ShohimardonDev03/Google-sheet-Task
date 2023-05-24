package uz.shohimardon.googlesheettask.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertDto {

    private String fullName;
    private String phone;
    private String course;
    private String branch;


}
