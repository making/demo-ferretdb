package com.example;

import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private final MongoTemplate mongoTemplate;

	public HelloController(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@PostMapping(path = "/messages")
	public Message postMessage(@RequestBody String text) {
		return mongoTemplate.save(new Message(null, text));
	}

	@GetMapping(path = "/messages")
	public List<Message> getMessages() {
		return mongoTemplate.findAll(Message.class);
	}

}
