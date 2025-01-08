package com.thehecklers.planefinder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Controller
public class PlaneController {

    private final PlaneFinderService pService;

    public PlaneController(PlaneFinderService pService) {
        this.pService = pService;
    }

    @ResponseBody
    @GetMapping("/aircraft")
    public Flux<Aircraft> getAircrafts() throws IOException {
        return pService.getAircraft();
    }


}
