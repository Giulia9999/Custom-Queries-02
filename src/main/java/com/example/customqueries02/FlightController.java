package com.example.customqueries02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/flights")
public class FlightController {
    @Autowired
    FlightRepo flightRepo;
    // provisioning of n flights (where n is an optional query param; if absent, n=100)
    @PostMapping("/provision")
    public Collection<Flight> provisionFlights(@RequestParam(required = false)Integer n){
        if(n==null){
            n = 100;
        }

        List<Flight> flightList = new ArrayList<>();
        FlightStatusEnum[] values = FlightStatusEnum.values();
        Random random = new Random();
        for (int i= 0; i<n; i++){
            Flight flight = new Flight();
            flight.setDescription(generateRandomString(random, 10));
            flight.setFromAirport(generateRandomString(random, 5));
            flight.setToAirport(generateRandomString(random, 6));

            FlightStatusEnum randomEnum = values[random.nextInt(values.length)];
            flight.setStatus(randomEnum);

            flightRepo.save(flight);
            flightList.add(flight);
        }
        return flightList;
    }
    private String generateRandomString(Random random, int length){
        return random.ints(97,123)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    //retrieving all the flights in the db using pagination and returning them
    // in ascending order by fromAirport
    @GetMapping
    public Page<Flight> getAllFlights(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("fromAirport").ascending());
        return flightRepo.findAll(pageable);
    }
    //retrieving all the flights that are ONTIME without using a custom query
    @GetMapping("/onTime")
    public Page<Flight> getFlightsOnTime(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return flightRepo.findByStatus(FlightStatusEnum.ONTIME,pageable);
    }

    //retrieving - using a custom query - all the flights that are in p1 or in p2 status
    @GetMapping("/p1p2")
    public Page<Flight> get2Statuses(@RequestParam FlightStatusEnum p1,
                                         @RequestParam FlightStatusEnum p2,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return flightRepo.findByStatus(p1, p2, pageable);
    }

    /*
     * DOCUMENTAZIONE API: https://documenter.getpostman.com/view/26043911/2s93eU4FGE
     */
}
