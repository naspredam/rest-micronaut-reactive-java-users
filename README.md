# rest-micronaut-reactive-java-users

This project has as objective to have a rest api build on:

- Gradle
- Java 14
- Micronaut 2.1.1
- MySQL (8.0.20)

## What the rest api will stand for?

The rest will have the resource:

```
/users
```

Where the endpoints exposed are:

| Method | Endpoint | Description  |
| ---    |:------- |:-----|
|GET| /users | Get all the users |
|POST| /users | Create a new user |
|GET| /users/{user_id} | Get specific user data |
|DELETE| /users/{user_id} | Delete specific user data |

## Default micronaut documentation

### Feature jdbc-hikari documentation

- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)

### Feature http-client documentation

- [Micronaut Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

### Feature hibernate-jpa documentation

- [Micronaut Hibernate JPA documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#hibernate)

