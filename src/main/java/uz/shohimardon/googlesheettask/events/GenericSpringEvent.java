package uz.shohimardon.googlesheettask.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class GenericSpringEvent<T> extends ApplicationEvent {
    private T what;
    protected boolean success;

    public GenericSpringEvent(T what, boolean success) {
        super(what);
        this.what = what;
        this.success = success;
    }

}