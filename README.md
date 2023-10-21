
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

#### Get all items

```http
  GET /api/items
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required**. Your API key |

#### Get item

```http
  GET /api/items/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. Id of item to fetch |

#### add(num1, num2)

Takes two numbers and returns the sum.


## Tech Stack

**Server:** Java, Spring Framework

