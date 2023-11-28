
![Logo](https://hobbie.s3.amazonaws.com/hobbie-logo.png)


# Hobbie - Find and Join Sports Events 🏃‍♂️⚽🎾

Welcome to Hobbie, your go-to app for discovering and participating in sports events that match your interests and location preferences. Whether you're an adrenaline junkie or simply looking to stay active, Hobbie connects you with like-minded individuals and exciting sporting opportunities.


## Documentation

[Documentation](https://hobbie.notion.site/Hobbie-Docs-Modeling-956d9fcfdb224447ae0effa20c246203?pvs=4)


## Environment Variables

To run this project, you will need to add the following environment variables to your .env file

`DATABASE_URL`

`DATABASE_PASSWORD`

`DATABASE_USERNAME`


## Run Locally

Clone the project

```bash
  git clone https://github.com/hobbiee/hobbie-core.git
```

Go to the project directory

```bash
  cd hobbie-core
```

Create .env file and keys from section above with your local values

```bash
  DATABASE_URL=jdbc:postgresql://localhost:5432/hobbie
  DATABASE_PASSWORD=admin
  DATABASE_USERNAME=postgres
```


## API Reference

We going to always try to do an effort to keep the API Reference on README updated, but if you noticed the README is outdated please take a look on swagger-ui from the Staging environment linked in the project description.

#### Create Username Password User

```http
POST /v1/api/auth/users
Accept 'Application/JSON'
```

```http
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "password": "string"
}
```

#### Username Password Jwt Authenticaiton

```http
POST /v1/api/auth/username-password
Accept 'Application/JSON'
```

```http
{
  "email": "string",
  "password": "string"
}
```

### Create Players

```http
POST /v1/api/players
Accept 'Multipart/Form-data'
```

| Key | Value         | Description |
| :-------- | :-------| :---------- |
| `name`  | `string`  | Player's name |
| `avatar`| `file`    | Player's profile image |
| `latitude`  | `float`  | Latitude to match events |
| `longitude`| `float`    | Longitude to match events |
| `radius`  | `float`  | Radius to match events |
| `birthDate`| `date`    | Player's birth date |
| `interests`| `string[]`    | Player's interests to match events |



#### Create Events

```http
POST /v1/api/events
Accept 'Application/JSON'
```

```http
{
  "name": "string",
  "description": "string",
  "capacity": 2,
  "startDate": "2023-11-28T03:56:15.391Z",
  "endDate": "2023-11-28T03:56:15.391Z",
  "latitude": 90,
  "longitude": 180,
  "categories": [
    "string"
  ],
  "adminId": 0
}
```

#### Upload Event Thumbnail

```http
PATCH /v1/api/events/{id}/thumbnail
Accept 'Multipart/form-data'
```
| Key | Value         | Description |
| :-------- | :-------| :---------- |
| `id`  | `integer`  | Event id |
| `file`| `file`    | Event thumbnail |

#### Send Participation Request

```http
POST /v1/api/players/interest
Accept 'Application/JSON'
```

```http
{
  "eventId": 0,
  "playerId": 0
}
```

#### Accept Join Request

```http
POST /v1/api/players/accept-join-request
Accept 'Application/JSON'
```

```http
{
  "adminId": 0,
  "playerToAcceptId": 0,
  "eventId": 0
}
```

#### Accept Join Request

```http
POST /v1/api/players/reject-join-request
Accept 'Application/JSON'
```

```http
{
  "adminId": 0,
  "playerToRejectId": 0,
  "eventId": 0
}
```


#### Find Events Matching With Interests

```http
GET /v1/api/events/{playerId}
```

| Key         | Value       | Description                     |
| :--------   | :-------    | :-------------------------------|
| `playerId`  | `integer`   | Player id to find events for    |

## Tech Stack

**Server:** Java, Spring Framework

