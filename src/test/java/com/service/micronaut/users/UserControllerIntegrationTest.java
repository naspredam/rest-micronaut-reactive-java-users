package com.service.micronaut.users;

import com.github.javafaker.Faker;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.List;

import static io.micronaut.http.HttpRequest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MicronautTest
public class UserControllerIntegrationTest {

    private static final Faker FAKER = new Faker();

    @Inject
    @Client("/")
    private RxHttpClient client;

    @Inject
    private EntityManager entityManager;

    @Test
    public void shouldPersistUserInformation() {
        UserData userData = UserData.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .phone(FAKER.phoneNumber().phoneNumber())
                .build();

        UserData userResponse = client.toBlocking()
                .retrieve(POST("/users", userData), Argument.of(UserData.class));

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull().isNotNegative();
        assertThat(userResponse.getFirstName()).isEqualTo(userData.getFirstName());
        assertThat(userResponse.getLastName()).isEqualTo(userData.getLastName());
        assertThat(userResponse.getPhone()).isEqualTo(userData.getPhone());
    }

    @Test
    public void shouldReturnEmptyListWhenNoInformationPersisted() {
        entityManager.createQuery("select u from UserData u", UserData.class)
                .getResultList()
                .forEach(entityManager::remove);
        entityManager.getTransaction().commit();

        List<UserData> users = client.toBlocking()
                .retrieve(GET("/users"), Argument.listOf(UserData.class));

        assertThat(users).isEmpty();
    }

    @Test
    public void shouldReturnTheUserPersisted() {
        UserData userData1 = UserData.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .phone(FAKER.phoneNumber().phoneNumber())
                .build();
        UserData userData2 = UserData.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .phone(FAKER.phoneNumber().phoneNumber())
                .build();
        entityManager.persist(userData1);
        entityManager.persist(userData2);
        entityManager.getTransaction().commit();

        List<UserData> usersResponse = client.toBlocking()
                .retrieve(GET("/users"), Argument.listOf(UserData.class));

        assertThat(usersResponse).hasSize(2)
                .containsExactlyInAnyOrder(userData1, userData2);
    }

    @Test
    public void shouldReturnTheUserPersistedWhenSearchingById() {
        UserData userData = UserData.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .phone(FAKER.phoneNumber().phoneNumber())
                .build();
        entityManager.persist(userData);
        entityManager.getTransaction().commit();

        UserData user = client.toBlocking()
                .retrieve(GET("/users/" + userData.getId()), Argument.of(UserData.class));

        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(userData);
    }

    @Test
    public void shouldDeleteUserPersisted() {
        UserData userData = UserData.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .phone(FAKER.phoneNumber().phoneNumber())
                .build();
        entityManager.persist(userData);
        entityManager.getTransaction().commit();

        HttpResponse<Object> deleteResponse = client.toBlocking()
                .exchange(DELETE("/users/" + userData.getId()));
        assertThat(deleteResponse.code()).isEqualTo(200);

        assertThatThrownBy(() -> client.toBlocking().retrieve(GET("/users/" + userData.getId())))
                .isInstanceOf(HttpClientResponseException.class);
    }
}