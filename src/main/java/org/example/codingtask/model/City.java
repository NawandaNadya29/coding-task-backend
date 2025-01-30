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
    private Integer id;
    private String name;
    private String ascii;
    private String altName;
    private Double lat;      
    private Double lon;     
    private String country;
    private String cc2;
    private String admin1; 
    private Long population;
    private String dem;
    private String tz;      
    private Double score;
}
