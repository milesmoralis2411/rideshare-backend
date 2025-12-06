package org.example.rideshare.service;

import org.example.rideshare.model.Ride;
import org.example.rideshare.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RideService {
    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public Ride createRide(Ride ride) {
        ride.setStatus("REQUESTED");
        return rideRepository.save(ride);
    }

    public List<Ride> listRequests() {
        return rideRepository.findByStatus("REQUESTED");
    }

    public Optional<Ride> acceptRide(String rideId, String driverId) {
        Optional<Ride> r = rideRepository.findById(rideId);
        if (r.isPresent() && "REQUESTED".equals(r.get().getStatus())) {
            Ride ride = r.get();
            ride.setDriverId(driverId);
            ride.setStatus("ACCEPTED");
            return Optional.of(rideRepository.save(ride));
        }
        return Optional.empty();
    }

    public Optional<Ride> completeRide(String rideId) {
        Optional<Ride> r = rideRepository.findById(rideId);
        if (r.isPresent() && "ACCEPTED".equals(r.get().getStatus())) {
            Ride ride = r.get();
            ride.setStatus("COMPLETED");
            return Optional.of(rideRepository.save(ride));
        }
        return Optional.empty();
    }

    public List<Ride> getUserRides(String userId) {
        return rideRepository.findByUserId(userId);
    }
}
