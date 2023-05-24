package uz.shohimardon.googlesheettask.dto;

import com.google.api.services.sheets.v4.model.GridRange;

public record DropListData(GridRange gridRange, String range) {

}