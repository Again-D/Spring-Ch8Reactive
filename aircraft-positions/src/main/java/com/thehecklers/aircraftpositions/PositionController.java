package com.thehecklers.aircraftpositions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

//@RequiredArgsConstructor
@Controller
public class PositionController {
    @NonNull
    private final AircraftRepository repository;
    // RSocket 통신을 위한 객체 주입
    private final RSocketRequester requester;

    private WebClient client =
            WebClient.create("http://localhost:7634/aircraft");

    PositionController(AircraftRepository repository,
                       RSocketRequester.Builder builder) {
        this.repository = repository;
        this.requester = builder.tcp("localhost",7635);
    }

    @GetMapping("/aircraft")
    public String getCurrentAircraftPositions(Model model) {
//        repository.deleteAll();

        Flux<Aircraft> aircrafts = repository.deleteAll()
                .thenMany(client.get()
                        .retrieve()
                        .bodyToFlux(Aircraft.class)
                        .filter(plane-> !plane.getReg().isEmpty())
                        .flatMap(repository::save));
//        client.get()
//                .retrieve()
//                .bodyToFlux(Aircraft.class)
//                .filter(plane -> !plane.getReg().isEmpty())
//                .toStream()
//                .forEach(repository::save);

        model.addAttribute("currentPositions", aircrafts);
        return "positions";
    }

    // RSocket 클라이언트 엔드포인트
    @ResponseBody
    @GetMapping(value = "/acstream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Aircraft> getCurrentACPositionStream() {
        return requester.route("acstream")
                .data("Requesting aircraft positions")
                .retrieveFlux(Aircraft.class);
    }
}