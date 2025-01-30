package org.example.codingtask.service;

import org.example.codingtask.model.City;
import org.example.codingtask.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    // Method to search for cities based on query 'q'
    public List<City> findCities(String q) {
        return cityRepository.findByNameContainingIgnoreCase(q);
    }

    // Method to retrieve all cities from the database
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    // Method to calculate the number of cities based on query 'q'
    public long countCities(String q) {
        return cityRepository.countByNameContainingIgnoreCase(q); // Count the number of cities containing 'q'
    }

    // Method to count the number of all cities in the database
    public long countAllCities() {
        return cityRepository.count(); // Count the number of all cities in the database
    }

    // Calculating scores based on distance
    public double calculateScore(City city, Double latitude, Double longitude) {
        double distance = calculateDistance(latitude, longitude, city.getLatitude(), city.getLongitude());
        double maxDistance = 1000;
        double score = 1.0 - (distance / maxDistance);
        score = Math.max(0, Math.min(1, score));
        return score;
    }

    // Calculating the distance between two points
    public double calculateDistance(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        final double radiusOfEarthInKilometers = 6371;
        double latitudeDifference = Math.toRadians(latitude2 - latitude1);
        double longitudeDifference = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2) +
                Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                        Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radiusOfEarthInKilometers * c;
    }
}
