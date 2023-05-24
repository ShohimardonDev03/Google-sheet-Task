package uz.shohimardon.googlesheettask.contoller;

import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.shohimardon.googlesheettask.dto.InsertDto;
import uz.shohimardon.googlesheettask.service.UpdateService;


import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
public class SheetController {
    private final UpdateService updateService;

    @PutMapping("/insert")
    public ResponseEntity intersData( @ParameterObject  InsertDto dto) throws GeneralSecurityException, IOException {

        updateService.checkAndStore(  dto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
