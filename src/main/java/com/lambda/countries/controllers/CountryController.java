package com.lambda.countries.controllers;

import com.lambda.countries.models.Country;
import com.lambda.countries.repository.ICountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CountryController
{
    // Connect the controller to the repository
    @Autowired
    private ICountryRepository countryRepo;

    // HELPER: Filter Countries based on a given letter
    private List<Country> findCountries(List<Country> list, CheckCountry tester)
    {
        List<Country> filteredList = new ArrayList<>();

        for(Country c : list)
        {
            if(tester.test(c)) filteredList.add(c);
        }

        return filteredList;
    }

    //  ROUTES

    // GET /names/all -> Return all the countries in the DB
    @GetMapping(value = "/names/all", produces = {"application/json"})
    public ResponseEntity<?> getAllCountries()
    {
        List<Country> countriesList = new ArrayList<>();

        countryRepo.findAll().iterator().forEachRemaining(countriesList::add);

        List<Map<String, Object>> returnList = new ArrayList<>();

        for(Country c : countriesList)
        {
            Map<String, Object> countryMap = new HashMap<>();
            countryMap.put("name", c.getName());
            countryMap.put("population", c.getPopulation());
            countryMap.put("landmasskm2", c.getLandmasskm2());
            countryMap.put("medianage", c.getMedianage());
            returnList.add(countryMap);
        }
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    // GET /names/start/:letter
    @GetMapping(value = "/names/start/{letter}", produces = {"application/json"})
    public ResponseEntity<?> getByFirstNameLetter(@PathVariable char letter)
    {
        List<Country> countryList = new ArrayList<>();

        countryRepo.findAll().iterator().forEachRemaining(countryList::add);

        List<Country> filteredList = findCountries(countryList, c -> c.getName().charAt(0) == letter);
        filteredList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));

        Map<String, List<Country>> payload = new HashMap<>();
        payload.put("countries", filteredList);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // GET /population/total
    @GetMapping(value = "/population/total", produces = {"application/json"})
    public ResponseEntity<?> getPopulationTotal()
    {
        long total = 0;
        List<Country> countryList = new ArrayList<>();

        countryRepo.findAll().iterator().forEachRemaining(countryList::add);

        for(Country c : countryList)
        {
            total = total + c.getPopulation();
        }

        Map<String, Long> payload = new HashMap<>();
        payload.put("populationToatl", total);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // GET /population/max
    @GetMapping(value = "/population/max", produces = {"application/json"})
    public ResponseEntity<?> getMaxPop()
    {
        List<Country> countryList = new ArrayList<>();
        countryRepo.findAll().iterator().forEachRemaining(countryList::add);

        // Sort only works with int so we have to cast from a long to a int
        countryList.sort((c1, c2) -> (int) (c2.getPopulation() - c1.getPopulation()));
        Map<String, Country> payload = new HashMap<>();
        payload.put("country", countryList.get(0));

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    // GET /population/min
    @GetMapping(value = "/population/min", produces = {"application/json"})
    public  ResponseEntity<?> getMinPopulation()
    {
        List<Country> countryList = new ArrayList<>();
        countryRepo.findAll().iterator().forEachRemaining(countryList::add);

        countryList.sort((c1, c2) -> (int) (c1.getPopulation() - c2.getPopulation()));

        Map<String, Country> payload = new HashMap<>();
        payload.put("country", countryList.get(0));

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }
}
