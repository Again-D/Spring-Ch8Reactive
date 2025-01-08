package com.thehecklers.planefinder;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class FlightGenerator {
    private final Random rnd = new Random();

    List<String> typeList = List.of("A319","A320","A321",
            "BE33","BE36",
            "B737","B739","B763",
            "C172","C402","C560",
            "E50P","E75L",
            "MD11",
            "PA28","PA32","PA46");

    Aircraft generate() {
        String csfn = "SAL" + rnd.nextInt(1000);

        return new Aircraft(csfn,
                "N" + String.format("%1$5s", rnd.nextInt(10000)).replace(' ', '0'),
                csfn,
                typeList.get(rnd.nextInt(typeList.size())),
                rnd.nextInt(40000),
                rnd.nextInt(360),
                rnd.ints(1, 100, 500).iterator().next().intValue(),
                rnd.doubles(1, 35d, 42d).iterator().next().floatValue(),
                rnd.doubles(1, -115d, -83d).iterator().next().floatValue());
    }
}

