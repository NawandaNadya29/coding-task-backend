package org.example.codingtask.controller;

import org.example.codingtask.model.City;
import org.example.codingtask.model.CityDTO;
import org.example.codingtask.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

@RestController
public class CityController {

    @Autowired
    private CityService cityService;

    // Endpoint to get a list of cities with search query 'q' (or all cities if q is not given)
    @GetMapping("/suggestions")
    public List<CityDTO> getCitySuggestions(@RequestParam(required = false) String q,
                                            @RequestParam(required = false) Double lat,
                                            @RequestParam(required = false) Double lon) {

        List<City> cities;

        // Search for cities based on query 'q', if none, retrieve all cities
        if (q != null && !q.isEmpty()) {
            cities = cityService.findCities(q);
        } else {
            cities = cityService.findAllCities();
        }

        boolean useUserCoordinates = (lat != null && lon != null);

        for (City city : cities) {
            if (city.getLat() != null && city.getLon() != null) {
                double score;

                if (useUserCoordinates) {
                    score = cityService.calculateScore(city, lat, lon);
                } else {
                    score = cityService.calculateScore(city, city.getLat(), city.getLon());
                }

                city.setScore(score);
            }
        }

        // Create a DecimalFormat object to round numbers to one decimal number
        DecimalFormat df = new DecimalFormat("#.#");

        List<CityDTO> suggestions = cities.stream()
                .map(city -> {
                    // City name format with province and country
                    String formattedName = city.getName();
                    if (city.getAdmin1() != null) {
                        formattedName += ", " + city.getAdmin1(); 
                    }
                    if (city.getCountry() != null) {
                        formattedName += ", " + city.getCountry();  
                    }

                    // Round the score with DecimalFormat and replace the comma with a period
                    String scoreFormatted = "0.0"; // Default value if score is null
                    if (city.getScore() != null) {
                        scoreFormatted = df.format(city.getScore()).replace(",", ".");
                    }

                    double roundedScore = Double.parseDouble(scoreFormatted);

                    // Return a CityDTO object with a rounded score
                    return new CityDTO(formattedName,
                            city.getLat(),
                            city.getLon(),
                            roundedScore);
                }).sorted((c1, c2) -> Double.compare(c2.getScore(), c1.getScore())).collect(Collectors.toList());

        return suggestions;
    }
}
