package uz.shohimardon.googlesheettask.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import uz.shohimardon.googlesheettask.dto.DropListData;
import uz.shohimardon.googlesheettask.utils.Where;


@Getter
@Setter
@ToString
public class DropListEvent extends ApplicationEvent {
    private DropListData what;
    private Where where;
    protected boolean success;


    public DropListEvent(DropListData what, boolean success) {
        super(what);
        this.what = what;
        this.success = success;
    }

}