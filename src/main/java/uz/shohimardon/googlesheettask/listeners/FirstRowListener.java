package uz.shohimardon.googlesheettask.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import uz.shohimardon.googlesheettask.service.google.GoogleService;

@RequiredArgsConstructor
@Component
public class FirstRowListener {

    public final GoogleService googleService;
    public final ApplicationEventPublisher applicationEventPublisher;

}
