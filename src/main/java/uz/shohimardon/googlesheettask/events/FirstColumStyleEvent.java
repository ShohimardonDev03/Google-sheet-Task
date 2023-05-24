package uz.shohimardon.googlesheettask.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class FirstColumStyleEvent extends ApplicationEvent {

    protected boolean success;


    public FirstColumStyleEvent( boolean success) {
        super(success);

        this.success = success;
    }
}