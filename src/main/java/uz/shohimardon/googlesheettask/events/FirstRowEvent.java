package uz.shohimardon.googlesheettask.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import uz.shohimardon.googlesheettask.dto.SheetDto;

@Getter
@Setter
@ToString
public class FirstRowEvent extends ApplicationEvent {

    private SheetDto what;
    protected boolean success;

    public FirstRowEvent(SheetDto what, boolean success) {
        super(what);
        this.what = what;
        this.success = success;
    }
}