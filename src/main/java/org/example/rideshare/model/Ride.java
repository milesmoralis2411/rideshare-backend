package org.example.rideshare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.Date;

@Document(collection = "rides")
@Data
public class Ride {
    @Id
    private String id;
    private String userId;
    private String driverId;
    private String pickupLocation;
    private String dropLocation;
    private String status; // REQUESTED, ACCEPTED, COMPLETED
    private Date createdAt = new Date();
}
