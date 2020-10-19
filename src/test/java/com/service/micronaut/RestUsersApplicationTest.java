package com.service.micronaut;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class RestUsersApplicationTest {

    @Inject
    private EmbeddedApplication application;

    @Test
    void shouldApplicationBeUp() {
        assertThat(application.isRunning()).isTrue();
    }

}
