# Rideshare – Complete Submission (Improved)

This repo is a full submission-ready backend for the RideShare assignment.

## What I improved
- Proper role-based protection: driver-only endpoints under `/api/v1/driver/**`
- Authentication service + DTO `AuthResponse` returned on login (token + user info)
- Custom exceptions (`NotFoundException`, `BadRequestException`) and a global exception handler
- Postman collection included: `postman_collection.json`
- Clear curl examples and testing instructions

## Setup (local)
1. Java 17+, Maven, and a running MongoDB instance.
2. Update `src/main/resources/application.properties`:
   - Set `spring.data.mongodb.uri` if needed.
   - Replace `jwt.secret` with a long random string (min 32 characters).

3. Build & run:
```bash
mvn -DskipTests clean package
mvn spring-boot:run
```

## Quick curl examples
Register user:
```bash
curl -s -X POST http://localhost:8081/api/auth/register \
 -H "Content-Type: application/json" \
 -d '{"username":"alice","password":"pass123","role":"ROLE_USER"}'
```

Login:
```bash
curl -s -X POST http://localhost:8081/api/auth/login \
 -H "Content-Type: application/json" \
 -d '{"username":"alice","password":"pass123"}'
# response -> {"token":"...","username":"alice","role":"ROLE_USER"}
```

Create ride (add token from login):
```bash
curl -s -X POST http://localhost:8081/api/v1/rides \
 -H "Authorization: Bearer <token>" \
 -H "Content-Type: application/json" \
 -d '{"pickupLocation":"A","dropLocation":"B"}'
```

Driver actions:
- Use `role":"ROLE_DRIVER"` when registering a driver.
- Get pending requests: `GET /api/v1/driver/rides/requests` (Authorization header required).
- Accept: `POST /api/v1/driver/rides/{rideId}/accept`

## Notes for grading
- Endpoints include validation and meaningful error responses.
- Passwords stored using BCrypt.
- Tokens returned on login; endpoints are stateless (JWT).
- You can import `postman_collection.json` to Postman to test flows quickly.

