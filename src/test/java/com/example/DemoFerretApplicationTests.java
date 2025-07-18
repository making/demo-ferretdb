package com.example;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoFerretApplicationTests {

	RestClient restClient;

	@BeforeEach
	void setUp(@LocalServerPort int port, @Autowired RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.defaultStatusHandler(statusCode -> true, (req, res) -> {
			/* NO-OP */}).baseUrl("http://localhost:" + port).build();
	}

	@Test
	void contextLoads() {
		{
			ResponseEntity<Message> res = this.restClient.post()
				.uri("/messages")
				.contentType(MediaType.TEXT_PLAIN)
				.body("Hello MongoDB!")
				.retrieve()
				.toEntity(Message.class);
			assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
			Message message = res.getBody();
			assertThat(message).isNotNull();
			assertThat(message.text()).isEqualTo("Hello MongoDB!");
			assertThat(message.id()).isNotNull();
		}
		{
			ResponseEntity<Message> res = this.restClient.post()
				.uri("/messages")
				.contentType(MediaType.TEXT_PLAIN)
				.body("Hello FerretDB!")
				.retrieve()
				.toEntity(Message.class);
			assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
			Message message = res.getBody();
			assertThat(message).isNotNull();
			assertThat(message.text()).isEqualTo("Hello FerretDB!");
			assertThat(message.id()).isNotNull();
		}
		{
			ResponseEntity<List<Message>> res = this.restClient.get()
				.uri("/messages")
				.retrieve()
				.toEntity(new ParameterizedTypeReference<>() {
				});
			assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
			List<Message> messages = res.getBody();
			assertThat(messages).isNotNull();
			assertThat(messages).hasSize(2);
			assertThat(messages).map(Message::id).allSatisfy(id -> assertThat(id).isNotNull());
			assertThat(messages).map(Message::text).containsExactly("Hello MongoDB!", "Hello FerretDB!");
		}
	}

}
