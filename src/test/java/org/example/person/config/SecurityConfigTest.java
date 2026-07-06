package org.example.person.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityConfigTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturn401WithoutAuth() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/persons", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturn200WithValidAuth() {
        ResponseEntity<String> response = restTemplate
            .withBasicAuth("admin", "admin123")
            .getForEntity("/api/v1/persons", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
