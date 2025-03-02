package com.thehecklers.planefinder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

import java.time.Instant;
// 5. Entity 수정...
//@Entity
@Table      // 버전에 따라서 필요한 경우 있음. // If you want this repository to be a R2DBC repository, consider annotating your entities with one of these annotations: org.springframework.data.relational.core.mapping.Table (preferred), or consider extending one of the following types with your repository: org.springframework.data.r2dbc.repository.R2dbcRepository
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aircraft {

    @Id
//    @GeneratedValue
    private Long id;
    private String callsign, squawk, reg, flightno, route, type, category;

    private int altitude, heading, speed;
    @JsonProperty("vert_rate")
    private double vertRate;
    @JsonProperty("selected_altitude")
    private int selectedAltitude;

    private double lat, lon, barometer;
    @JsonProperty("polar_distance")
    private double polarDistance;
    @JsonProperty("polar_bearing")
    private double polarBearing;

    @JsonProperty("is_adsb")
    private boolean isADSB;
    @JsonProperty("is_on_ground")
    private boolean isOnGround;

    @JsonProperty("last_seen_time")
    private Instant lastSeenTime;
    @JsonProperty("pos_update_time")
    private Instant posUpdateTime;
    @JsonProperty("bds40_seen_time")
    private Instant bds40SeenTime;

    public Aircraft(String callsign, String reg, String flightno, String type, int altitude, int heading, int speed, double lat, double lon){
        this(null, callsign, "sqwk", reg, flightno, "route", type, "ct", altitude, heading, speed, 0, 0, lat, lon, 0D, 0D, 0D, false, true, Instant.now(), Instant.now(), Instant.now());
    }

    public void setLastSeenTime(long lastSeenTime) {
        this.lastSeenTime = Instant.ofEpochSecond(lastSeenTime);
    }

    public void setPosUpdateTime(long posUpdateTime) {
        this.posUpdateTime = Instant.ofEpochSecond(posUpdateTime);
    }

    public void setBds40SeenTime(long bds40SeenTime) {
        this.bds40SeenTime = Instant.ofEpochSecond(bds40SeenTime);
    }
}
