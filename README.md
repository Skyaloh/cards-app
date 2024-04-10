# Cards Application

This is an application for cards management by users

## Features
- OAuth2 authentication with Spring Security
- RESTful API endpoints (Users & Cards)

# Getting Started

### Authentication Endpoints

- `http://localhost:8082/oauth/token`: Endpoint for obtaining OAuth2 access tokens.

API Documentation

http://localhost:8082/swagger-ui/index.html

Testclient:
 - client_id: read-write
 - client_secret: testclient

Test User:
 - username: admin@gmail.com
 - password: admin1234
 - grant_type: password

Authentication sample
```
curl --location 'http://localhost:8082/oauth/token' \
--header 'Authorization: Basic cmVhZC13cml0ZTp0ZXN0Y2xpZW50' \
--form 'grant_type="password"' \
--form 'username="admin"' \
--form 'password="admin1234"'

```

##Card Endpoints

```
curl --location 'http://localhost:8080/api/v1/card?page=0&size=20' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2Utc2VydmVyLXJlc3QtYXBpIl0sInVzZXJfbmFtZSI6ImFkbWluQGdtYWlsLmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3MTI1MzcwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiNGRkYjRjMzUtNzYzMy00NDQxLWJjMjUtNDUwMDliMzQ4NmUxIiwiY2xpZW50X2lkIjoicmVhZC13cml0ZSJ9.hRSCW5oeyDSRTEzmd8eGSdl4yMT9mTHOqup3RpxAcxo'
```

#User Endpoints

```
curl --location 'http://localhost:8080/api/v1/user?page=0&size=20' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2Utc2VydmVyLXJlc3QtYXBpIl0sInVzZXJfbmFtZSI6ImFkbWluQGdtYWlsLmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE3MTI1MzcwMTIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiNGRkYjRjMzUtNzYzMy00NDQxLWJjMjUtNDUwMDliMzQ4NmUxIiwiY2xpZW50X2lkIjoicmVhZC13cml0ZSJ9.hRSCW5oeyDSRTEzmd8eGSdl4yMT9mTHOqup3RpxAcxo'

```
