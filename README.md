
![Logo](https://hobbie.s3.us-east-1.amazonaws.com/hobbie-logo.png?response-content-disposition=inline&X-Amz-Security-Token=IQoJb3JpZ2luX2VjELT%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCXNhLWVhc3QtMSJHMEUCIGhRzbb0CmcFss4muq0UWxqBKU5GWn5%2F6vBpNugjaJ1gAiEAx8qVqULvut3NFeWTro6Ib%2BKEjX8jcMhOggfq92WM54sq7QIInf%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FARAAGgw0MDk4NzM0MTg1MDIiDDNF7yZhWeq44rFe4SrBAjerYfJJ6kEHYNwgi6L7m7t7DnE5MisdUxkHP0%2BnKFQ0O8Zt3gJKcGkSm9c%2BsCahdJwdj50aafy283y8PqXgNP2cNxKqzpYCPWKqvsdO29ZtddYJ45hsdPAhnVeddvKes01wb3lA%2FKVD0SRejAWlA0%2Fm1hyjNWPvgToJhpwgCGvRcz0YwoPCy3WCWHeiNPCVQPvM6QUcDnQr5QHkww99%2FJ7Mpo8NGuyvXpyHLHM3NaJ%2BtWK2Xj8rqQqfymN6DOGL3TF%2BONo2Q1GNj6rFtI3pVGPHvvUj2%2FpNkyaUhADTrHlI%2BgJLg8VZpHL0G9CRMeHj7FiRNny1GxAbMwgTq4SuGWDgi38TWGkXC%2FxE5%2Fl4HC4eQxRqPvEMD5zJJwQ%2FDUMSQ3cCjKY%2FgMJqW7OxgGx4Y3DsNqSVkALEhyFg6qT4yA%2BjvDD4iZ%2BoBjqzAoAHD3lHpnbGcO31BKuRGdGeupa6VJ%2ByNJchTt0EPKSBm7AII3Iuq%2BkBBZsZK2WzhnUfkprWEaK%2FlzT6ZXi25bc6ABn2rq4UTtoTWPGwUWDc9xTMC1KA4EBOC5hRaMF6jb5vButlNpU%2B5KcE2Q7RmTLb7pTAzzwsDOg%2F6AJRnpIlofNKsztgYb7pylebpzsntJt2ozlZaB3gf3vU4Z0fUItEXJZg88LMzMMkwFgZQT2yvW7MGJ2IfB65cXG9y%2Bsxh8%2F%2FfsApjShxUghA%2FuPCymLlHGIgNF94XPg9q0WRPGXmS5lAAFgJTbgiHfzhqEDVhOr%2FyGLtrXADRkFktCvUVnpLULkQTsAQNAX2C7gHrp20H7D0R4V5xNctwAG3IvnhIbh5d4XIiF2ocGUQXsc6VR6Sc9c%3D&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230918T033655Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Credential=ASIAV63S4BEDMGUKMQF3%2F20230918%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=dbbafb45c23b434b1f822873f87495695c3a4b12795e62c406c1a4185e65bf13)


# Hobbie - Find and Join Sports Events 🏃‍♂️⚽🎾

Welcome to Hobbie, your go-to app for discovering and participating in sports events that match your interests and location preferences. Whether you're an adrenaline junkie or simply looking to stay active, Hobbie connects you with like-minded individuals and exciting sporting opportunities.


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


## Documentation

[Documentation](https://hobbie.notion.site/Hobbie-Docs-Modeling-956d9fcfdb224447ae0effa20c246203?pvs=4)


## Environment Variables

To run this project, you will need to add the following environment variables to your .env file

`DATABASE_URL`

`DATABASE_PASSWORD`

`DATABASE_USERNAME`

