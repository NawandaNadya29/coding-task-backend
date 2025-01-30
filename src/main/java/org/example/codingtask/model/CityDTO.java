package org.example.codingtask.model;

public class CityDTO {
    private String name;
    private Double lat;     
    private Double long;      
    private Double score;

    // Constructor
    public CityDTO(String name, Double lat, Double long, Double score) {
        this.name = name;
        this.lat = lat;
        this.lon = long;
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

    public Double getLong() {
        return long;
    }

    public void setLong(Double long) {
        this.long = long;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
