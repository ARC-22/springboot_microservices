package com.arc.microservices.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

//@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateProduct(){
		String requestBody = """
				{
					"name": "Product 1",
					"description": "Product 1 description",
					"price": 100
				}
				""";
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("name", Matchers.equalTo("Product 1"))
				.body("description", Matchers.equalTo("Product 1 description"))
				.body("price", Matchers.equalTo(100))
				.body("id", Matchers.notNullValue());
	}

}
