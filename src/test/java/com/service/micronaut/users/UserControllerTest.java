package com.service.micronaut.users;

import com.github.javafaker.Faker;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.List;

import static io.micronaut.http.HttpRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class UserControllerTest {

    private static final Faker FAKER = new Faker();

    @Inject
    @Client("/")
    private RxHttpClient client;

    @Inject
    private UserRepository userRepository;

    @MockBean(UserRepository.class)
    public UserRepository mockUserRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Test
    public void shouldPersistUserInformation() {
        long userId = FAKER.number().numberBetween(1, 10000);
        UserData userData = UserData.builder()
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .phone(FAKER.phoneNumber().phoneNumber())
                .build();

        Mockito.when(userRepository.save(userData))
                .thenReturn(userData.toBuilder().id(userId).build());

        HttpResponse<UserData> userHttpResponse = client.toBlocking()
                .exchange(POST("/users", userData), Argument.of(UserData.class));

        assertThat(userHttpResponse.code()).isEqualTo(200);
        UserData userDataResponse = userHttpResponse.body();
        assertThat(userDataResponse).isNotNull();
        assertThat(userDataResponse.getId()).isNotNull().isNotNegative().isEqualTo(userId);
        assertThat(userDataResponse.getFirstName()).isEqualTo(userData.getFirstName());
        assertThat(userDataResponse.getLastName()).isEqualTo(userData.getLastName());
        assertThat(userDataResponse.getPhone()).isEqualTo(userData.getPhone());
    }

    @Test
    public void shouldReturnEmptyListWhenNoInformationPersisted() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of());

        HttpResponse<List<UserData>> usersHttpResponse = client.toBlocking()
                .exchange(GET("/users"), Argument.listOf(UserData.class));

        assertThat(usersHttpResponse.code()).isEqualTo(200);
        assertThat(usersHttpResponse.body()).isEmpty();
        Mockito.verify(userRepository).findAll();
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
        Mockito.when(userRepository.findAll()).thenReturn(List.of(userData1, userData2));

        HttpResponse<List<UserData>> usersHttpResponse = client.toBlocking()
                .exchange(GET("/users"), Argument.listOf(UserData.class));

        assertThat(usersHttpResponse.code()).isEqualTo(200);
        assertThat(usersHttpResponse.body()).hasSize(2)
                .containsExactlyInAnyOrder(userData1, userData2);
    }

    @Test
    public void shouldReturnTheUserPersistedWhenSearchingById() {
        Long userId = FAKER.number().numberBetween(0, 10000L);
        UserData userData = UserData.builder()
                .id(userId)
                .firstName(FAKER.name().firstName())
                .lastName(FAKER.name().lastName())
                .phone(FAKER.phoneNumber().phoneNumber())
                .build();

        Mockito.when(userRepository.findById(userId)).thenReturn(userData);

        HttpResponse<UserData> userHttpResponse = client.toBlocking()
                .exchange(GET("/users/" + userData.getId()), Argument.of(UserData.class));

        assertThat(userHttpResponse.code()).isEqualTo(200);
        assertThat(userHttpResponse.body()).isEqualTo(userData);
    }

    @Test
    public void shouldDeleteUserPersisted() {
        Long userId = FAKER.number().numberBetween(0, 10000L);

        HttpResponse<Object> userHttpResponse = client.toBlocking()
                .exchange(DELETE("/users/" + userId));

        assertThat(userHttpResponse.code()).isEqualTo(200);
        Mockito.verify(userRepository).deleteById(userId);
    }
}
