package org.example.codingtask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "geoname")
@Data
public class City {
    @Id
    private Integer geonameid;
    private String name;
    private String asciiname;
    private String alternatenames;
    private Double latitude;
    private Double longitude;
    private String countryCode;
    private String admin1Code;
    private Long population;
    private String timezone;
    private Double score;
}
