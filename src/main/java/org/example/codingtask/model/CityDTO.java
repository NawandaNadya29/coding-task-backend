package org.example.codingtask.model;

public class CityDTO {
    private String name;
    private Double lat;     
    private Double lon;      
    private Double score;

    // Constructor
    public CityDTO(String name, Double lat, Double lon, Double score) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.score = score;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
