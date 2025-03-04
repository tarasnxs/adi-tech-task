package com.epam.aditechtask;

import com.epam.aditechtask.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdiTechTaskApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private static Long categoryId;
	private static Long productId;
	private static String jwtToken;

	private String getBaseUrl(String path) {
		return "http://localhost:" + port + path;
	}

	private HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(jwtToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	@Test
	@Order(1)
	void shouldAuthenticateAndRetrieveToken() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String loginPayload = """
            {
                "username": "admin",
                "password": "password"
            }
        """;

		HttpEntity<String> request = new HttpEntity<>(loginPayload, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(
			getBaseUrl("/auth/login"), request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).isNotEmpty();

		jwtToken = response.getBody();
	}

	@Test
	@Order(2)
	void shouldCreateCategory() {
		CategoryRequest request = new CategoryRequest("Electronics", Optional.empty());

		HttpEntity<CategoryRequest> entity = new HttpEntity<>(request, getAuthHeaders());

		ResponseEntity<CategoryResponse> response = restTemplate.exchange(
			getBaseUrl("/categories"), HttpMethod.POST, entity, CategoryResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().name()).isEqualTo("Electronics");

		categoryId = response.getBody().id();
		assertThat(categoryId).isNotNull();
	}

	@Test
	@Order(3)
	void shouldCreateProduct() {
		ProductRequest request = new ProductRequest(
			"iPhone 13 Pro",
			new BigDecimal("999.99"),
			categoryId,
			Optional.of("EUR")
		);

		HttpEntity<ProductRequest> entity = new HttpEntity<>(request, getAuthHeaders());

		ResponseEntity<ProductResponse> response = restTemplate.exchange(
			getBaseUrl("/products"), HttpMethod.POST, entity, ProductResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().name()).isEqualTo("iPhone 13 Pro");

		productId = response.getBody().id();
		assertThat(productId).isNotNull();
	}

	@Test
	@Order(4)
	void shouldRetrieveProduct() {
		HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());

		ResponseEntity<ProductResponse> response = restTemplate.exchange(
			getBaseUrl("/products/" + productId), HttpMethod.GET, entity, ProductResponse.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().name()).isEqualTo("iPhone 13 Pro");
	}

	@Test
	@Order(5)
	void shouldDeleteProduct() {
		HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());

		ResponseEntity<Void> response = restTemplate.exchange(
			getBaseUrl("/products/" + productId), HttpMethod.DELETE, entity, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	@Order(6)
	void shouldDeleteCategory() {
		HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());

		ResponseEntity<Void> response = restTemplate.exchange(
			getBaseUrl("/categories/" + categoryId), HttpMethod.DELETE, entity, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}
