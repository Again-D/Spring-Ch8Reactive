package com.thehecklers.planefinder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PlaneFinderService {
    private final PlaneRepository repo;
    private final FlightGenerator generator;
    private URL acURL;
    private final ObjectMapper om;

    @SneakyThrows
    public PlaneFinderService(PlaneRepository repo, FlightGenerator generator) {
        this.repo = repo;
        this.generator = generator;

//        acURL = new URL("http://192.168.3.192/ajax/aircraft");
        acURL = new URL("http://localhost/ajax/aircraft");
        om = new ObjectMapper();
    }

    // 3. repository에 인터페이스를 교체 후 반환값을 변경..
    // T -> Mono<T> , Iterable<T> -> Flux<T>
    Flux<Aircraft> getAircraft() throws IOException {
        List<Aircraft> positions = new ArrayList<>();

        JsonNode aircraftNodes = null;
        try {
            aircraftNodes = om.readTree(acURL.openStream()).get("aircraft");
            aircraftNodes.iterator().forEachRemaining(node -> {
                try{
                    positions.add(om.treeToValue(node, Aircraft.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println("\n>>> IO Exception: "+e.getLocalizedMessage()+", generating and providing sample date.\n");
            return saveSamplePositions();
        }

        if (positions.size() > 0) {
            positions.forEach(System.out::println);

            repo.deleteAll();
            return repo.saveAll(positions);
        }else {
            System.out.println("\n>>> No positions to report, generating and provideing sample data.\n");
            return saveSamplePositions();
        }
    }

    private Flux<Aircraft> saveSamplePositions() {
        final Random rnd = new Random();

        for(int i=0; i<rnd.nextInt(15); i++) {
            repo.save(generator.generate())
                    .thenMany(repo.findAll())
                    .subscribe();
        }
        return repo.findAll();

//        repo.deleteAll();
//        for(int i=0; i<rnd.nextInt(15); i++) {
//            repo.save(generator.generate());
//        }
//        return repo.findAll();
    }
}