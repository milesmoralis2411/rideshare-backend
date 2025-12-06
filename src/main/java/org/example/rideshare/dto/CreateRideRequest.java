package org.example.rideshare.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRideRequest {
    @NotBlank
    private String pickupLocation;
    @NotBlank
    private String dropLocation;
}
