package com.thehecklers.planefinder;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

@Controller
public class PlaneController {

    private final PlaneFinderService pService;

    public PlaneController(PlaneFinderService pService) {
        this.pService = pService;
    }

    // 4. PlaneFinderService와 연계된 Controller의 값 조정..
    @ResponseBody
    @GetMapping("/aircraft")
    public Flux<Aircraft> getAircrafts() throws IOException {
        return pService.getAircraft();
    }
    
    // RSocket 적용 - 2) 통신을 위한 연결 메서드 생성
    @MessageMapping("acstream")     // 경로 기반이 아니어서 "/"를 사용하지 않음.
    public Flux<Aircraft> getCurrentAircrafts() throws IOException {
        return pService.getAircraft().concatWith(
                Flux.interval(Duration.ofSeconds(1))
                        .flatMap(
                                l -> {
                                    try {
                                        return pService.getAircraft();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
        );
    }
    

}
