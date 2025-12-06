package org.example.rideshare.controller;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;
    private final UserRepository userRepository;

    public RideController(RideService rideService, UserRepository userRepository) {
        this.rideService = rideService;
        this.userRepository = userRepository;
    }

    // USER endpoint - create a ride
    @PostMapping("/rides")
    public ResponseEntity<?> createRide(@Valid @RequestBody CreateRideRequest req, Authentication auth) {
        String username = (String) auth.getPrincipal();
        User u = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("user_not_found"));
        Ride r = new Ride();
        r.setPickupLocation(req.getPickupLocation());
        r.setDropLocation(req.getDropLocation());
        r.setUserId(u.getId());
        Ride saved = rideService.createRide(r);
        return ResponseEntity.ok(saved);
    }

    // USER endpoint - get rides for user
    @GetMapping("/user/rides")
    public ResponseEntity<?> getUserRides(Authentication auth) {
        String username = (String) auth.getPrincipal();
        User u = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("user_not_found"));
        List<Ride> rides = rideService.getUserRides(u.getId());
        return ResponseEntity.ok(rides);
    }

    // DRIVER endpoint - list pending requests
    @GetMapping("/driver/rides/requests")
    public ResponseEntity<?> listRequests() {
        List<Ride> reqs = rideService.listRequests();
        return ResponseEntity.ok(reqs);
    }

    // DRIVER endpoint - accept
    @PostMapping("/driver/rides/{rideId}/accept")
    public ResponseEntity<?> acceptRide(@PathVariable String rideId, Authentication auth) {
        String username = (String) auth.getPrincipal();
        User u = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("user_not_found"));
        var res = rideService.acceptRide(rideId, u.getId());
        return res.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(java.util.Map.of("error","cannot_accept")));
    }

    // Either driver or user completes (we'll allow driver to mark complete)
    @PostMapping("/rides/{rideId}/complete")
    public ResponseEntity<?> completeRide(@PathVariable String rideId, Authentication auth) {
        var res = rideService.completeRide(rideId);
        return res.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().body(java.util.Map.of("error","cannot_complete")));
    }
}
