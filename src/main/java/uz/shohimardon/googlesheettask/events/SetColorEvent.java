package uz.shohimardon.googlesheettask.events;

import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.GridRange;
import lombok.*;
import org.springframework.context.ApplicationEvent;
import uz.shohimardon.googlesheettask.dto.DropListData;
import uz.shohimardon.googlesheettask.utils.Where;

import java.time.Clock;


@Getter
@Setter
@ToString
@Builder

public class SetColorEvent extends ApplicationEvent {
    private GridRange grid;
    private Color color;
    private String text;
    @Builder.Default
    protected boolean success = true;

    public SetColorEvent(GridRange grid, Color color, String text, boolean success) {
        super(grid);
        this.grid = grid;
        this.color = color;
        this.text = text;
        this.success = success;
    }


}