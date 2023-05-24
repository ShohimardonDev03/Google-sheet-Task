package uz.shohimardon.googlesheettask.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class ResizeEvent extends ApplicationEvent {

    private Integer what;
    protected boolean success;

    public ResizeEvent(Integer what, boolean success) {
        super(what);
        this.what = what;
        this.success = success;
    }
}